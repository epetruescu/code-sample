package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "phases")
//A signal phase has its own configuration as its independent of others since it's based on the traffic itself
//This is a signal phase.
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phase_id")
    private int phaseId;

    @Column(name = "intersection_id", nullable = false, insertable = false, updatable = false)
    private int intersectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private int greenDuration;

    @Column(nullable = false)
    private int yellowDuration;

    @OneToMany(mappedBy = "phase")
    private List<SignalGroupPhase> signalGroupPhases;

    @Version
    private Integer version;

    public void validate() {
        if (yellowDuration < 1) {
            throw new IllegalArgumentException("Yellow duration must be >= 1 second");
        }
        if (greenDuration < 1) {
            throw new IllegalArgumentException("Green duration must be >= 1 second");
        }
        if (greenDuration < yellowDuration) {
            throw new IllegalArgumentException("Green duration must be >= yellow duration");
        }
    }
}
