package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionRequest;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class IntersectionService {

    @Resource
    private IntersectionRepository intersectionRepository;

    @Resource
    private SignalGroupService signalGroupService;

    @Resource
    private PhaseService phaseService;

    public Intersection findById(int id) {
        return intersectionRepository.findById(id).get();
    }

    public Intersection createIntersection(IntersectionRequest request) {
        Intersection intersection = Intersection.builder()
                .name(request.name()).active(request.active()).build();
        return intersectionRepository.save(intersection);
    }

    public void update(Integer id, IntersectionRequest request) {
        intersectionRepository.updateNameAndActiveByIntersectionIdEquals(request.name(), request.active(), id);
    }

    public void deleteIntersection(int id) {
        Intersection intersection = findById(id);
        if (intersection == null) {
            throw new EntityNotFoundException("Intersection not found with id: " + id);
        }
        if (intersection.isActive()) {
            throw new DataIntegrityViolationException("Intersection is still active. Unable to delete");
        }
        signalGroupService.deleteAllByIntersectionId(id);
        phaseService.deleteAllByIntersectionId(id);
        intersectionRepository.deleteById(id);
    }
}
