package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "intersections")
public class Intersection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intersection_id")
    private int intersectionId;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = false;

    @Builder.Default
    @Column(nullable = false)
    private int currentPhaseIndex = 0;

    @Builder.Default
    @Column(nullable = false)
    private Instant lastTransitionTime = Instant.now();

    @Version
    private Integer version;
}
