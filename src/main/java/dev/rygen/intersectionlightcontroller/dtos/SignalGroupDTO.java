package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.enums.LightColor;

public record SignalGroupDTO(
        Integer id,
        String name,
        LightColor currentColor
) {
    public static SignalGroupDTO fromEntity(SignalGroup signalGroup) {
        return new SignalGroupDTO(
                signalGroup.getSignalGroupId(),
                signalGroup.getName(),
                signalGroup.getCurrentColor()
        );
    }
}
