package dev.rygen.intersectionlightcontroller.dtos.requests;

import dev.rygen.intersectionlightcontroller.enums.LightColor;

public record SignalGroupCreateRequest(
        Integer intersectionId,
        String name,
        LightColor currentColor
) {
}
