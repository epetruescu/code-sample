package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "signal_groups")
//a signal group is attached to phases.
public class SignalGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signal_group_id")
    private int signalGroupId;

    @Column(name = "intersection_id", nullable = false, insertable = false, updatable = false)
    private int intersectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LightColor currentColor = LightColor.RED;

    @Builder.Default
    @OneToMany(mappedBy = "signalGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SignalGroupPhase> signalGroupPhases = new ArrayList<>();
}
