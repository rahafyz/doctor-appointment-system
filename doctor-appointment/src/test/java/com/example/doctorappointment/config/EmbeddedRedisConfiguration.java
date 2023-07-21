package com.example.doctorappointment.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;



@TestConfiguration
public class EmbeddedRedisConfiguration {

    private static RedisServer redisServer = null;

    @PostConstruct
    public void postConstruct() {
        if (redisServer == null) {
            redisServer = RedisServer.builder().port(6379).build();
            redisServer.start();
        }
    }

    @PreDestroy
    public void preDestroy() {
        if (redisServer != null) {
            redisServer.stop();
            redisServer = null;
        }
    }

}