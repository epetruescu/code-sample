package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.PhaseDTO;
import dev.rygen.intersectionlightcontroller.dtos.requests.PhaseCreateRequest;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.entities.SignalGroupPhase;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.PhaseRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupPhaseRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PhaseService {

    @Resource
    private PhaseRepository phaseRepository;

    @Resource
    private IntersectionRepository intersectionRepository;

    @Resource
    private SignalGroupService signalGroupService;

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
    public Phase create(PhaseCreateRequest request) {
        if (request.signalGroupIds().isEmpty()) {
            throw new IllegalArgumentException("Signal Group Ids are required");
        }
        if (!intersectionRepository.existsById(request.intersectionId())) {
            throw new EntityNotFoundException("Intersection not found with Id: " + request.intersectionId());
        }
        log.info("Received request {} with sequence {}", request, request.sequence());

        Phase phase = Phase.builder()
                .intersectionId(request.intersectionId())
                .sequence(request.sequence())
                .greenDuration(request.greenDuration())
                .yellowDuration(request.yellowDuration())
                .build();
        phase.validate();
        phase = phaseRepository.save(phase);

        for (Integer signalGroupId : request.signalGroupIds()) {
            if (!signalGroupService.existsById(signalGroupId)) {
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
                if (!signalGroupService.existsById(signalGroupId)) {
                    throw new EntityNotFoundException("Signal group not found: " + signalGroupId);
                }

                SignalGroup signalGroup = signalGroupService.findById(signalGroupId);
                
                if (signalGroup.getIntersectionId() != phase.getIntersectionId()) {
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
        phaseRepository.deleteById(id);
    }

    public void deleteAllByIntersectionId(Integer intersectionId) {
        phaseRepository.deleteByIntersectionIdEquals(intersectionId);
    }

    public Phase findByIntersectionIdAndPhaseSequence(Integer intersectionId, Integer phaseSequence) {
        return phaseRepository.findByIntersectionIdEqualsAndSequenceEquals(intersectionId, phaseSequence);
    }

    public long countByIntersectionId(Integer intersectionId) {
        return phaseRepository.countByIntersectionIdEquals(intersectionId);
    }

    @Transactional
    public void updatePhaseGroup(Integer intersectionId, Integer sequence, LightColor currentLight) {
        Phase phase = findByIntersectionIdAndPhaseSequence(intersectionId, sequence);
        List<SignalGroupPhase> signalGroupPhases = signalGroupPhaseRepository.findByPhaseId(phase.getPhaseId());
        signalGroupPhases.forEach((signalGroupPhase -> {
            signalGroupService.updateLight(signalGroupPhase.getSignalGroupId(), currentLight);
        }));
    }
}
