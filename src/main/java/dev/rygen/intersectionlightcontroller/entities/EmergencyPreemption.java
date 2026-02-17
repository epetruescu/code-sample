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
//Sets a signal group and phase as active sort of a thing for emergency vehicles
public class EmergencyPreemption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "intersection_id", nullable = false, insertable = false, updatable = false)
    private int intersectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    @Column(name = "priority_signal_group_id", nullable = false, insertable = false, updatable = false)
    private int prioritySignalGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_signal_group_id")
    private SignalGroup prioritySignalGroup;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = false;

    @Column(nullable = false)
    private Instant activatedAt;

    @Builder.Default
    @Column(nullable = false)
    private Integer durationSeconds = 20;
}
