package dev.rygen.intersectionlightcontroller.services;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LeadershipElectionService {

    public static final String INTERSECTION_LEADER_KEY = "intersection:leader:";
    @Resource
    RedissonClient redissonClient;

    private final Map<Integer, RLock> heldLocks = new ConcurrentHashMap<>();

    public boolean tryBecomeLeader(Integer intersectionId) {
        RLock lock = redissonClient.getLock(INTERSECTION_LEADER_KEY + intersectionId);
        try {
            //This is autorenewed
            boolean acquired = lock.tryLock(0, TimeUnit.SECONDS);
            if (acquired) {
                heldLocks.put(intersectionId, lock);
                log.info("Successfully acquired leadership for intersection {}", intersectionId);
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while acquiring lock for intersection {}", intersectionId, e);
            return false;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("Error while getting lock {}", intersectionId, e);
            return false;
        }
    }

    public void releaseLeadership(Integer intersectionId) {
        RLock lock = heldLocks.get(intersectionId);

        if (lock == null) {
            log.debug("Leadership not held for intersection {}", intersectionId);
            return;
        }
        try {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Releasing leadership for intersection {}", intersectionId);
            } else if (lock.isLocked()) {
                log.warn("Lock for intersection {} is not held by current thread", intersectionId);
                lock.forceUnlock();
            }

        } catch (Exception e) {
            lock.forceUnlock();
            log.error("Error while releasing leadership for intersection {}", intersectionId);
        } finally {
            heldLocks.remove(intersectionId);
        }

    }

    public boolean isLeader(Integer intersectionId) {
        return heldLocks.containsKey(intersectionId);
    }

    public boolean renewLeadership(Integer intersectionId) {
        RLock lock = heldLocks.get(intersectionId);

        if (lock == null) {
            log.warn("Attempt to renew leadership for intersection {} but no lock held", intersectionId);
            return false;
        }
        if (!lock.isLocked()) {
            log.warn("Lock for intersection {} is no longer held", intersectionId);
            heldLocks.remove(intersectionId);
            return false;
        }
        return true;
    }
}
