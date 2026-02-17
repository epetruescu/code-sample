package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.PhaseDTO;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.entities.SignalGroupPhase;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.PhaseRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupPhaseRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public class PhaseService {

    @Resource
    private PhaseRepository phaseRepository;

    @Resource
    private IntersectionRepository intersectionRepository;

    @Resource
    private SignalGroupRepository signalGroupRepository;

    @Resource
    private SignalGroupPhaseRepository signalGroupPhaseRepository;

    public List<Phase> findByIntersectionId(Integer intersectionId) {
        if (!intersectionRepository.existsById(intersectionId)) {
            throw new EntityNotFoundException("Intersection not found with Id: " + intersectionId);
        }
        return phaseRepository.findByIntersectionIdEquals(intersectionId);
    }

    public Phase findById(Integer id) {
        return phaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Phase not found with id: " + id)
        );
    }

    @Transactional
    public Phase create(PhaseDTO request) {
        if (request.signalGroupIds().isEmpty()) {
            throw new IllegalArgumentException("Signal Group Ids are required");
        }
        if (!intersectionRepository.existsById(request.intersectionId())) {
            throw new EntityNotFoundException("Intersection not found with Id: " + request.intersectionId());
        }

        Phase phase = Phase.builder()
                .intersectionId(request.intersectionId())
                .sequence(request.sequence())
                .greenDuration(request.greenDuration())
                .yellowDuration(request.yellowDuration())
                .build();
        phase.validate();
        phase = phaseRepository.save(phase);

        for (Integer signalGroupId : request.signalGroupIds()) {
            if (!signalGroupRepository.existsById(signalGroupId)) {
                throw new EntityNotFoundException("Signal group not found: " + signalGroupId);
            }

            SignalGroupPhase sgp = SignalGroupPhase.builder()
                    .phaseId(phase.getPhaseId())
                    .signalGroupId(signalGroupId)
                    .build();
            signalGroupPhaseRepository.save(sgp);
        }

        return phase;
    }

    @Transactional
    public Phase update(Integer id, PhaseDTO request) {
        Phase phase = findById(id);
        if (request.intersectionId() != null && !request.intersectionId().equals(phase.getIntersectionId())) {
            throw new IllegalArgumentException("Cannot change intersection for existing phase");
        }

        if (request.sequence() != null) {
            phase.setSequence(request.sequence());
        }
        if (request.greenDuration() != null) {
            phase.setGreenDuration(request.greenDuration());
        }
        if (request.yellowDuration() != null) {
            phase.setYellowDuration(request.yellowDuration());
        }
        phase.validate();

        if (request.signalGroupIds() != null && !request.signalGroupIds().isEmpty()) {
            signalGroupPhaseRepository.deleteByPhaseId(phase.getPhaseId());

            for (Integer signalGroupId : request.signalGroupIds()) {
                if (!signalGroupRepository.existsById(signalGroupId)) {
                    throw new EntityNotFoundException("Signal group not found: " + signalGroupId);
                }

                SignalGroup signalGroup = signalGroupRepository.findById(signalGroupId)
                        .orElseThrow(() -> new EntityNotFoundException("Signal group not found: " + signalGroupId));
                
                if (signalGroup.getIntersectionId() == phase.getIntersectionId()) {
                    throw new IllegalArgumentException(
                            "Signal group " + signalGroupId + " does not belong to intersection " + phase.getIntersectionId()
                    );
                }

                SignalGroupPhase sgp = SignalGroupPhase.builder()
                        .phaseId(phase.getPhaseId())
                        .signalGroupId(signalGroupId)
                        .build();
                signalGroupPhaseRepository.save(sgp);
            }
        }
        
        return phaseRepository.save(phase);
    }

    public void delete(Integer id) {
        //Can be deleted and phase state change would happen else where
        phaseRepository.deleteById(id);
    }

    public void deleteAllByIntersectionId(Integer intersectionId) {
        phaseRepository.deleteByIntersectionIdEquals(intersectionId);
    }
}
