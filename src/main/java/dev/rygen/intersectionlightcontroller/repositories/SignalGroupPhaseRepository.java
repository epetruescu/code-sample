package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.SignalGroupPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalGroupPhaseRepository extends JpaRepository<SignalGroupPhase, Integer> {

    List<SignalGroupPhase> findByPhaseId(Integer phaseId);

    List<SignalGroupPhase> findBySignalGroupId(Integer signalGroupId);

    void deleteByPhaseId(Integer phaseId);

    void deleteBySignalGroupId(Integer signalGroupId);
}
