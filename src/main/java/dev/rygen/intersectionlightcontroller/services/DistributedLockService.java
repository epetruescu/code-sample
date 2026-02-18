package dev.rygen.intersectionlightcontroller.services;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@Slf4j
public class DistributedLockService {
    public static final String INTERSECTION_STATE_LOCK_KEY = "intersection:state:lock:";
    @Resource
    private RedissonClient redissonClient;


    public String getIntersectionStateLockKey(Integer intersectionId) {
        return INTERSECTION_STATE_LOCK_KEY + intersectionId;
    }

    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit,
                                 Supplier<T> action) {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, unit);

            if (!acquired) {
                throw new IllegalStateException("Could not acquire lock: " + lockKey);
            }

            try {
                log.debug("Acquired lock: {}", lockKey);
                return action.get();
            } finally {
                lock.unlock();
                log.debug("Released lock: {}", lockKey);
            }

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while trying to execute with lock for: " + lockKey, e);
        }
    }

    public void executeWithLock(String lockKey, long waitTime, long leaseTime,
                                TimeUnit unit, Runnable action) {
        executeWithLock(lockKey, waitTime, leaseTime, unit, () -> {
            action.run();
            return null;
        });
    }
}
