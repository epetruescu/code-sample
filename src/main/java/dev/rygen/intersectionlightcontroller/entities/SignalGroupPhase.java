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
@Table(name = "signal_group_phases")
//Signal groups can be attached to multiple phases
//A phase can have multiple signal groups
//only one phase is active at a time though
public class SignalGroupPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phase_id", nullable = false, insertable = false, updatable = false)
    private int phaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id")
    private Phase phase;

    @Column(name = "signal_group_id", nullable = false, insertable = false, updatable = false)
    private int signalGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signal_group_id")
    private SignalGroup signalGroup;
}
