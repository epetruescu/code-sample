package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Integer> {
    List<Phase> findByIntersectionIdEquals(int intersectionId);

    void deleteByIntersectionIdEquals(int intersectionId);

    Phase findByIntersectionIdEqualsAndSequenceEquals(int intersectionId, Integer sequence);

    long countByIntersectionIdEquals(int intersectionId);
}
