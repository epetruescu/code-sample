package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.entities.SignalGroupPhase;

import java.util.List;

public record PhaseDTO(
        Integer id,
        Integer intersectionId,
        Integer sequence,
        Integer greenDuration,
        Integer yellowDuration,
        List<Integer> signalGroupIds)
{
    public static PhaseDTO fromEntity(Phase phase) {
        return new PhaseDTO(
                phase.getPhaseId(),
                phase.getIntersectionId(),
                phase.getSequence(),
                phase.getGreenDuration(),
                phase.getYellowDuration(),
                phase.getSignalGroupPhases() != null
                        ? phase.getSignalGroupPhases().stream()
                        .map(SignalGroupPhase::getSignalGroupId)
                        .toList()
                        : List.of()
        );
    }
}
