package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class WorkerService {

    public static final String INTERSECTION_STATE_KEY = "intersection:state:";
    public static final String CYCLE_COUNT = "cycleCount";
    public static final String LAST_TRANSITION_TIME = "lastTransitionTime";
    public static final String PHASE_INDEX = "phaseIndex";
    public static final String CURRENT_LIGHT = "CurrentLight";
    @Resource
    private DistributedLockService distributedLockService;

    @Resource
    private LeadershipElectionService leadershipElectionService;

    @Resource
    private IntersectionService intersectionService;

    @Resource
    private PhaseService phaseService;

    @Resource
    private ScheduledExecutorService scheduledExecutorService;

    @Resource
    private RedissonClient redissonClient;

    private final Map<Integer, ScheduledFuture<?>> runningIntersections = new ConcurrentHashMap<>();

    @Value("${intersection.leadership.lease-duration:30}")
    private long leadershipLeaseDuration;

    @Value("${intersection.worker.interval:1}")
    private long workerCycleInterval;

    @PostConstruct
    public void init() {
        try {
            scheduledExecutorService.scheduleAtFixedRate(
                    () -> intersectionService.findAllIdsActive().forEach(this::startIntersection),
                    0, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Unable to get leadership");
        }
    }

    public void startIntersection(Integer intersectionId) {
        log.debug("Starting intersection {}", intersectionId);
        if (runningIntersections.containsKey(intersectionId)) {
            log.info("Intersection {} is already running", intersectionId);
            return;
        }
        if (!leadershipElectionService.tryBecomeLeader(intersectionId, leadershipLeaseDuration)) {
            log.debug("Unable to acquire leadership {}", intersectionId);
            return;
        }
        try {
            Intersection intersection = intersectionService.getIntersection(intersectionId);
            if (!intersection.isActive()) {
                log.info("Intersection {} not active", intersectionId);
                leadershipElectionService.releaseLeadership(intersectionId);
                return;
            }

            ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(
                    () -> ownershipCycle(intersectionId),
                    0,
                    workerCycleInterval,
                    TimeUnit.SECONDS
            );
            runningIntersections.put(intersectionId, future);
        } catch (Exception e) {
            log.error("Failed to start intersection {}", intersectionId, e);
            leadershipElectionService.releaseLeadership(intersectionId);
        }
    }

    private void ownershipCycle(Integer intersectionId) {
        try {
            if (!leadershipElectionService.isLeader(intersectionId)) {
                stopIntersection(intersectionId);
                return;
            }
            boolean renewed = leadershipElectionService.renewLeadership(intersectionId, leadershipLeaseDuration);
            if (!renewed) {
                stopIntersection(intersectionId);
                return;
            }
            String lockKey = distributedLockService.getIntersectionStateLockKey(intersectionId);
            distributedLockService.executeWithLock(lockKey, 100, 5000, TimeUnit.MILLISECONDS, () -> checkPhases(intersectionId)
            );
        } catch (Exception e) {
            log.error("Error with intersection cycle for {}", intersectionId);
        }
    }

    public void stopIntersection(Integer intersectionId) {
        ScheduledFuture<?> future = runningIntersections.remove(intersectionId);
        if (future != null) {
            future.cancel(false);
        }
        leadershipElectionService.releaseLeadership(intersectionId);
    }

    private void checkPhases(Integer intersectionId) {
        RMap<String, Object> state = redissonClient.getMap(INTERSECTION_STATE_KEY + intersectionId);
        Integer phaseIndex = (Integer) state.getOrDefault(PHASE_INDEX, 0);
        Instant lastTransitionTime = (Instant) state.getOrDefault(LAST_TRANSITION_TIME, Instant.now());
        Long cycleCount = (Long) state.getOrDefault(CYCLE_COUNT, 0L);

        Phase currentPhase = phaseService.findByIntersectionIdAndPhaseSequence(intersectionId, phaseIndex);
        Instant currentTime = Instant.now();

        if (currentTime.isBefore(lastTransitionTime.plusSeconds(currentPhase.getGreenDuration()))) {
            updateSignalColor(LightColor.GREEN, intersectionId, currentPhase, state);
        } else if(currentTime.isBefore(lastTransitionTime.plusSeconds(
                currentPhase.getGreenDuration() + currentPhase.getYellowDuration()))) {
            updateSignalColor(LightColor.YELLOW, intersectionId, currentPhase, state);
        } else {
           nextPhase(intersectionId, currentPhase, currentTime, state);
        }

        state.put("cycleCount", cycleCount + 1);
    }

    private void nextPhase(Integer intersectionId, Phase currentPhase, Instant currentTime, RMap<String, Object> state) {
        long numberOfPhases = phaseService.countByIntersectionId(intersectionId);
        int nextIndex = (currentPhase.getSequence() + 1) % (int) numberOfPhases;

        //update intersection by Id with current phase color and next phase color
        state.fastPut(PHASE_INDEX, nextIndex);
        state.fastPut(CURRENT_LIGHT, LightColor.GREEN.name());
        state.fastPut(LAST_TRANSITION_TIME, currentTime);
        intersectionService.updateIntersectionState(intersectionId, currentPhase.getSequence(), LightColor.RED,
                nextIndex, LightColor.GREEN, currentTime);
    }

    private void updateSignalColor(LightColor currentLightColor, Integer intersectionId, Phase currentPhase,
                                   RMap<String, Object> state) {

        LightColor stateLightColor = (LightColor) state.getOrDefault(CURRENT_LIGHT, LightColor.GREEN);

        if (stateLightColor != currentLightColor) {
            state.put(CURRENT_LIGHT, currentLightColor);
            phaseService.updatePhaseGroup(intersectionId, currentPhase.getSequence(), currentLightColor);
        }
    }
}
