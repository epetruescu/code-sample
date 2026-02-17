package dev.rygen.intersectionlightcontroller.services;

import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class DistributedLockService {
    @Resource
    private RedissonClient redissonClient;


}
