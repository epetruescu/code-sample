package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IntersectionRepository extends JpaRepository<Intersection, Integer> {
    @Transactional
    @Modifying
    @Query("update Intersection i set i.name = ?1, i.active = ?2 where i.intersectionId = ?3")
    void updateNameAndActiveByIntersectionIdEquals(@NonNull String name, boolean active, int intersectionId);
}
