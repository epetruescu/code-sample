package dev.rygen.intersectionlightcontroller.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PhaseCreateRequest(
        @NotNull Integer intersectionId,
        @NotNull Integer sequence,
        @NotNull Integer greenDuration,
        @NotNull Integer yellowDuration,
        @NotEmpty List<Integer> signalGroupIds
) {
}
