package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionRequest;
import dev.rygen.intersectionlightcontroller.dtos.PhaseDTO;
import dev.rygen.intersectionlightcontroller.dtos.SignalGroupDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.PhaseRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntersectionService {

    @Resource
    private IntersectionRepository intersectionRepository;

    @Resource
    private PhaseRepository phaseRepository;

    @Resource
    private SignalGroupRepository signalGroupRepository;

    @Resource
    private SignalGroupService signalGroupService;

    @Resource
    private PhaseService phaseService;

    public IntersectionDTO findById(Integer id) {
        Intersection intersection = intersectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intersection not found with id: " + id));
        List<Phase> phases = phaseRepository.findByIntersectionIdEquals(id);
        List<SignalGroup> signalGroups = signalGroupRepository.findByIntersectionIdEquals(id);
        return IntersectionDTO.fromEntityWithCollections(intersection, phases, signalGroups);
    }

    @Transactional
    public IntersectionDTO create(IntersectionRequest request) {
        //Name is just a user description and doesnt have to be unique
        //in a real system it would be set apart from others by its physical location in lat and long
        Intersection intersection = Intersection.builder()
                .name(request.name())
                .active(false)
                .build();
        return IntersectionDTO.fromEntity(intersectionRepository.save(intersection));
    }

    @Transactional
    public IntersectionDTO update(Integer id, IntersectionRequest request) {
        Intersection intersection = intersectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intersection not found with id: " + id));

        if (StringUtils.isNotBlank(request.name())) {
            intersection.setName(request.name());
        }

        List<Phase> phases = phaseRepository.findByIntersectionIdEquals(id);
        List<SignalGroup> signalGroups = signalGroupRepository.findByIntersectionIdEquals(id);

        if (request.active() !=null && request.active()) {
            if (phases.isEmpty() || signalGroups.isEmpty()) {
                throw new IllegalStateException("Intersection " + id + " requires at least one Phase and Signal Group");
            }
            intersection.setActive(true);
        }
        
        intersection = intersectionRepository.save(intersection);
        return IntersectionDTO.fromEntityWithCollections(intersection, phases, signalGroups);
    }

    @Transactional
    public void deleteIntersection(Integer id) {
        Intersection intersection = intersectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intersection not found with id: " + id));

        if (intersection.isActive()) {
            throw new IllegalStateException("Intersection is still active. Unable to delete");
        }
        phaseService.deleteAllByIntersectionId(id);
        signalGroupService.deleteAllByIntersectionId(id);
        intersectionRepository.deleteById(id);
    }
}
