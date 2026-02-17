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
                intersection.getPhases() != null
                        ? intersection.getPhases().stream()
                        .map(PhaseDTO::fromEntity)
                        .toList()
                        : List.of(),
                intersection.getSignalGroups() != null
                        ? intersection.getSignalGroups().stream()
                        .map(SignalGroupDTO::fromEntity)
                        .toList()
                        : List.of()
        );
    }
}
