package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "emergency_preemption")
public class EmergencyPreemption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "intersection_id", nullable = false)
    private int intersectionId;

    @Column(name = "priority_signal_group_id", nullable = false)
    private int prioritySignalGroupId;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = false;

    @Column(nullable = false)
    private Instant activatedAt;

    @Builder.Default
    @Column(nullable = false)
    private Integer durationSeconds = 20;
}
