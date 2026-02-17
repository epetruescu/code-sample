package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phase_id")
    private int phaseId;

    @Column(name = "intersection_id", nullable = false)
    private int intersectionId;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private int greenDuration;

    @Column(nullable = false)
    private int yellowDuration;

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
