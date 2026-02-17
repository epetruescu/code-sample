package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class WorkerService {

    @Resource
    private DistributedLockService distributedLockService;

    @Resource
    private LeadershipElectionService leadershipElectionService;

    @Resource
    private IntersectionService intersectionService;

    @Resource
    private ScheduledExecutorService scheduledExecutorService;

    private final Map<Integer, ScheduledFuture<?>> runningIntersections = new ConcurrentHashMap<>();

    public boolean startIntersection(Integer intersectionId) {
        log.debug("Starting intersection {}", intersectionId);
        if (runningIntersections.containsKey(intersectionId)) {
            log.info("Intersection {} is already running", intersectionId);
            return true;
        }
        IntersectionDTO intersectionDTO = intersectionService.findById(intersectionId);
        if (!intersectionDTO.active()) {
            log.info("Intersection {} not active", intersectionId);
            return false;
        }
        return true;
    }
}
