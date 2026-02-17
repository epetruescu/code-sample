package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

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

    @Value("${intersection.leadership.lease-duration:30000}")
    private long leadershipLeaseDuration;

    @Value("${intersection.worker.interval:1}")
    private long workerCycleInterval;

    public boolean startIntersection(Integer intersectionId) {
        log.debug("Starting intersection {}", intersectionId);
        if (runningIntersections.containsKey(intersectionId)) {
            log.info("Intersection {} is already running", intersectionId);
            return true;
        }
        if (!leadershipElectionService.tryBecomeLeader(intersectionId, leadershipLeaseDuration)) {
            log.debug("Unable to acquire leadership {}", intersectionId);
            return false;
        }
        try {
            Intersection intersection = intersectionService.getIntersection(intersectionId);
            if (!intersection.isActive()) {
                log.info("Intersection {} not active", intersectionId);
                leadershipElectionService.releaseLeadership(intersectionId);
                return false;
            }

            ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(
                    () -> intersectionCycle(intersectionId),
                    0,
                    workerCycleInterval,
                    TimeUnit.SECONDS
            );
            runningIntersections.put(intersectionId, future);
            return true;
        } catch (Exception e) {
            log.error("Failed to start intersection {}", intersectionId, e);
            leadershipElectionService.releaseLeadership(intersectionId);
            return false;
        }
    }

    @Async
    //Return a boolean just to make testing easier
    private CompletableFuture<Boolean> intersectionCycle(Integer intersectionId) {
        try {
            if (!leadershipElectionService.isLeader(intersectionId)) {
                stopIntersection(intersectionId);
                return CompletableFuture.completedFuture(Boolean.FALSE);
            }
            boolean renewed = leadershipElectionService.renewLeadership(intersectionId, leadershipLeaseDuration);
            if (!renewed) {
                stopIntersection(intersectionId);
                return CompletableFuture.completedFuture(Boolean.FALSE);
            }
            return CompletableFuture.completedFuture(Boolean.TRUE);
        } catch (Exception e) {
            log.error("Error with intersection cycle for {}", intersectionId);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
    }

    private void stopIntersection(Integer intersectionId) {
        ScheduledFuture<?> future = runningIntersections.remove(intersectionId);
        if (future != null) {
            future.cancel(false);
        }
        leadershipElectionService.releaseLeadership(intersectionId);
    }
}
