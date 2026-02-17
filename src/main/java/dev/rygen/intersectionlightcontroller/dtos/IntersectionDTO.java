package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record IntersectionDTO(
        Integer id,
        String name,
        boolean active,
        Instant lastTransitionTime,
        Integer currentPhaseIndex,
        List<PhaseDTO> phases,
        List<SignalGroupDTO> signalGroups

) {
    public static IntersectionDTO fromEntity(Intersection intersection) {
        return new IntersectionDTO(
                intersection.getIntersectionId(),
                intersection.getName(),
                intersection.isActive(),
                intersection.getLastTransitionTime(),
                intersection.getCurrentPhaseIndex(),
                List.of(),
                List.of()
        );
    }

    public static IntersectionDTO fromEntityWithCollections(
            Intersection intersection,
            List<Phase> phases,
            List<SignalGroup> signalGroups) {
        
        List<PhaseDTO> phaseDTOs = phases != null 
                ? phases.stream().map(PhaseDTO::fromEntity).toList() 
                : List.of();
        
        List<SignalGroupDTO> signalGroupDTOs = signalGroups != null 
                ? signalGroups.stream().map(SignalGroupDTO::fromEntity).toList() 
                : List.of();
        
        return new IntersectionDTO(
                intersection.getIntersectionId(),
                intersection.getName(),
                intersection.isActive(),
                intersection.getLastTransitionTime(),
                intersection.getCurrentPhaseIndex(),
                phaseDTOs,
                signalGroupDTOs
        );
    }
}
