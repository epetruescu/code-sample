package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SignalGroupRepository extends JpaRepository<SignalGroup, Integer> {
    List<SignalGroup> findByIntersectionIdEqualsOrderBySignalGroupIdAsc(int intersectionId);
    
    List<SignalGroup> findByIntersectionIdEquals(int intersectionId);

    @Transactional
    @Modifying
    @Query("update SignalGroup s set s.name = ?1 where s.signalGroupId = ?2")
    void updateNameBySignalGroupIdEquals(String name, int signalGroupId);

    long deleteByIntersectionIdEquals(int intersectionId);

    long deleteBySignalGroupIdAndIntersectionIdEquals(int signalGroupId, int intersectionId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update SignalGroup s set s.currentColor = ?1 where s.signalGroupId = ?2")
    int updateCurrentColorBySignalGroupIdEquals(LightColor currentColor, int signalGroupId);
}
