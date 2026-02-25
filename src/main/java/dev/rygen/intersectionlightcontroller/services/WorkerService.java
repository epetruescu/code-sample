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
    public static final String CURRENT_LIGHT = "currentLight";
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

    private final Map<Integer, Integer> errorCounts = new ConcurrentHashMap<>();

    private static final int MAX_CONSECUTIVE_ERRORS = 5;

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
        if (!leadershipElectionService.tryBecomeLeader(intersectionId)) {
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

    public boolean ownershipCycle(Integer intersectionId) {
        try {
            if (!leadershipElectionService.isLeader(intersectionId)) {
                stopIntersection(intersectionId);
                log.debug("Not Intersection Leader: {}", intersectionId);
                return false;
            }
            boolean renewed = leadershipElectionService.renewLeadership(intersectionId);
            if (!renewed) {
                stopIntersection(intersectionId);
                log.debug("Unable to renew Intersection Leader: {}", intersectionId);
                return false;
            }
            String lockKey = distributedLockService.getIntersectionStateLockKey(intersectionId);
            distributedLockService.executeWithLock(lockKey, 100, 5000, TimeUnit.MILLISECONDS,
                    () -> checkPhases(intersectionId)
            );
            errorCounts.remove(intersectionId);
        } catch (Exception e) {
            int count = errorCounts.compute(intersectionId, (k, v) -> (v == null) ? 1 : v + 1);
            log.error("Error with intersection cycle for {} message: {}", intersectionId, e.getMessage(), e);
            if (count >= MAX_CONSECUTIVE_ERRORS) {
                log.error("Intersection {} has failed {} consecutive times",
                        intersectionId, count);
                stopIntersection(intersectionId);
                errorCounts.remove(intersectionId);
                return false;
            }
        }
        return true;
    }

    public void stopIntersection(Integer intersectionId) {
        ScheduledFuture<?> future = runningIntersections.remove(intersectionId);
        if (future != null) {
            future.cancel(false);
        }
        leadershipElectionService.releaseLeadership(intersectionId);
        errorCounts.remove(intersectionId);
    }

    private void checkPhases(Integer intersectionId) {
        log.debug("Check phase for Intersection {}", intersectionId);
        RMap<String, Object> state = redissonClient.getMap(INTERSECTION_STATE_KEY + intersectionId);
        Integer phaseIndex = (Integer) state.getOrDefault(PHASE_INDEX, 0);
        Instant lastTransitionTime = (Instant) state.get(LAST_TRANSITION_TIME);
        Long cycleCount = (Long) state.getOrDefault(CYCLE_COUNT, 0L);

        if (lastTransitionTime == null) {
            lastTransitionTime = Instant.now();
            state.put(LAST_TRANSITION_TIME, lastTransitionTime);
        }

        Phase currentPhase = phaseService.findByIntersectionIdAndPhaseSequence(intersectionId, phaseIndex);
        if (currentPhase == null) {
            log.warn("No phase found for intersection {} and phase index {}", intersectionId, phaseIndex);
            return;
        }
        log.debug("Check phase for phase sequence {}", currentPhase.getSequence());
        Instant currentTime = Instant.now();
        long secondsSinceTransition = currentTime.getEpochSecond() - lastTransitionTime.getEpochSecond();

        if (secondsSinceTransition < currentPhase.getGreenDuration()) {
            updateSignalColor(LightColor.GREEN, intersectionId, currentPhase, state);
        } else if(secondsSinceTransition < currentPhase.getGreenDuration() + currentPhase.getYellowDuration()) {
            updateSignalColor(LightColor.YELLOW, intersectionId, currentPhase, state);
        } else {
           nextPhase(intersectionId, currentPhase, currentTime, state);
        }

        state.put("cycleCount", cycleCount + 1);
    }

    private void nextPhase(Integer intersectionId, Phase currentPhase, Instant currentTime, RMap<String, Object> state) {
        List<Phase> phases = phaseService.findByIntersectionId(intersectionId);
        List<Integer> sequences = phases.stream().map(Phase::getSequence).toList();
        if (sequences.isEmpty()) {
            log.error("No Phases found for intersection {}", intersectionId);
            return;
        }
        int currentIndex = sequences.indexOf(currentPhase.getSequence());

        if (currentIndex == -1) {
            log.error("Current phase {} not found in phase list for intersection {}",
                    currentPhase.getSequence(), intersectionId);
            return;
        }

        int nextIndex = (currentIndex + 1) % sequences.size();
        Integer nextSequence = sequences.get(nextIndex);

        log.debug("Next phase for intersection {} sequence {}, and next {}", intersectionId, currentIndex, nextSequence);

        //update intersection by Id with current phase color and next phase color
        state.fastPut(PHASE_INDEX, nextSequence);
        state.fastPut(CURRENT_LIGHT, LightColor.GREEN);  // FIX: Store enum, not string
        state.fastPut(LAST_TRANSITION_TIME, currentTime);
        intersectionService.updateIntersectionState(intersectionId, currentPhase.getSequence(), LightColor.RED,
                nextSequence, LightColor.GREEN, currentTime);
    }

    private void updateSignalColor(LightColor currentLightColor, Integer intersectionId, Phase currentPhase,
                                   RMap<String, Object> state) {

        LightColor stateLightColor = (LightColor) state.get(CURRENT_LIGHT);
        log.debug("Updating signal for intersection {} to color {}", intersectionId, currentLightColor);

        if (stateLightColor == null || stateLightColor != currentLightColor) {
            state.put(CURRENT_LIGHT, currentLightColor);
            phaseService.updatePhaseGroup(intersectionId, currentPhase.getSequence(), currentLightColor);
        }
    }
}
