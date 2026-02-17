package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
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
            List<PhaseDTO> phases,
            List<SignalGroupDTO> signalGroups) {
        return new IntersectionDTO(
                intersection.getIntersectionId(),
                intersection.getName(),
                intersection.isActive(),
                intersection.getLastTransitionTime(),
                intersection.getCurrentPhaseIndex(),
                phases != null ? phases : List.of(),
                signalGroups != null ? signalGroups : List.of()
        );
    }
}
