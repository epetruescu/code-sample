package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.PhaseDTO;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.PhaseRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class PhaseService {

    @Resource
    private PhaseRepository phaseRepository;

    @Resource
    private IntersectionRepository intersectionRepository;

    public List<Phase> findByIntersectionId(Integer intersectionId) {
        if (!intersectionRepository.existsById(intersectionId)) {
            throw new EntityNotFoundException("Intersection not found with Id: " + intersectionId);
        }
        return phaseRepository.findByIntersectionIdEquals(intersectionId);
    }

    public Phase findById(Integer id) {
        return phaseRepository.findById(id).orElseThrow();
    }

    public Phase create(PhaseDTO request) {
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
        return phaseRepository.save(phase);

    }

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
        return phaseRepository.save(phase);
    }

    public void delete(Integer id) {
        phaseRepository.deleteById(id);
    }

    public void deleteAllByIntersectionId(Integer intersectionId) {
        phaseRepository.deleteByIntersectionIdEquals(intersectionId);
    }
}
