package com.example.bloombackend.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Configuration
@Profile("test")
public class EmbeddedRedisConfig {

    private final RedisServer redisServer;

    public EmbeddedRedisConfig(
            @Value("${spring.data.redis.port}") int port) {
        this.redisServer = new RedisServer(port);
    }

    @PostConstruct
    public void start() {
        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
    }
}