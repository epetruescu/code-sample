package dev.rygen.intersectionlightcontroller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class Config {

    @Bean
    public ScheduledExecutorService scheduledExecutorService(@Value("${thread-pool:2}") int threadpool) {
        return Executors.newScheduledThreadPool(threadpool);
    }
}
