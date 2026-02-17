package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Phase;

import java.util.List;

public record PhaseDTO(
        Integer id,
        Integer intersectionId,
        Integer sequence,
        Integer greenDuration,
        Integer yellowDuration,
        List<Integer> signalGroupIds
) {
    public static PhaseDTO fromEntity(Phase phase) {
        return new PhaseDTO(
                phase.getPhaseId(),
                phase.getIntersectionId(),
                phase.getSequence(),
                phase.getGreenDuration(),
                phase.getYellowDuration(),
                List.of()
        );
    }

    public static PhaseDTO fromEntityWithSignalGroups(Phase phase, List<Integer> signalGroupIds) {
        return new PhaseDTO(
                phase.getPhaseId(),
                phase.getIntersectionId(),
                phase.getSequence(),
                phase.getGreenDuration(),
                phase.getYellowDuration(),
                signalGroupIds != null ? signalGroupIds : List.of()
        );
    }
}
