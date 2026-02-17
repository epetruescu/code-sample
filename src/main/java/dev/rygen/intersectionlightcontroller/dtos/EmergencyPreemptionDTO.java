package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.EmergencyPreemption;

import java.time.Instant;

public record EmergencyPreemptionDTO(
        Integer id,

        Integer intersectionId,

        Integer prioritySignalGroupId,
        
        Boolean active,
        
        Instant activatedAt,

        Integer durationSeconds,
        
        String reason
) {
    public EmergencyPreemptionDTO {
        if (durationSeconds == null) {
            durationSeconds = 20;  // Default 20 seconds
        }
    }
    
    public static EmergencyPreemptionDTO fromEntity(EmergencyPreemption preemption) {
        return new EmergencyPreemptionDTO(
                preemption.getId(),
                preemption.getIntersectionId(),
                preemption.getPrioritySignalGroupId(),
                preemption.getActive(),
                preemption.getActivatedAt(),
                preemption.getDurationSeconds(),
                null
        );
    }
}
