package dev.rygen.intersectionlightcontroller.services;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LeadershipElectionService {

    public static final String INTERSECTION_LEADER_KEY = "intersection:leader:";
    @Resource
    RedissonClient redissonClient;

    public boolean tryBecomeLeader(Integer intersectionId, long leadershipLeaseDuration) {
        RLock lock = redissonClient.getLock(INTERSECTION_LEADER_KEY + intersectionId);
        try {
            return lock.tryLock(0, leadershipLeaseDuration, TimeUnit.SECONDS);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("Error while getting lock {}", intersectionId, e);
            return false;
        }
    }

    public void releaseLeadership(Integer intersectionId) {
        RLock lock = redissonClient.getLock(INTERSECTION_LEADER_KEY + intersectionId);
        if (lock.isHeldByCurrentThread()) {
            try {
                lock.unlock();
                log.info("Released leadership for intersection {}", intersectionId);
            } catch (Exception e) {
                log.warn("Unable to release lock for intersection {}", intersectionId);
            }
        }
    }

    public boolean isLeader(Integer intersectionId) {
        RLock lock = redissonClient.getLock(INTERSECTION_LEADER_KEY + intersectionId);
        return lock.isHeldByCurrentThread();
    }

    public boolean renewLeadership(Integer intersectionId, long leadershipLeaseDuration) {
        RLock lock = redissonClient.getLock(INTERSECTION_LEADER_KEY + intersectionId);
        if (!lock.isHeldByCurrentThread()) {
            return false;
        }

        try {
            boolean renewed = lock.tryLock(0, leadershipLeaseDuration, TimeUnit.SECONDS);
            if (!renewed) {
                log.warn("Failed to renew leadership for intersection {}", intersectionId);
            }
            return renewed;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("Error while renewing leadership for intersection {}", intersectionId, e);
            return false;
        }
    }
}
