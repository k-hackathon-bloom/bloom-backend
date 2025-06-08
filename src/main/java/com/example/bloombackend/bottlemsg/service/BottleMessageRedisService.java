package com.example.bloombackend.bottlemsg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BottleMessageRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_KEY = "bottleMessage:senders";

    public void addSender(Long userId) {
        redisTemplate.opsForSet().add(REDIS_KEY, userId.toString());
    }

    public Set<String> getAllSenders() {
        return redisTemplate.opsForSet().members(REDIS_KEY);
    }

    public void clearSenders() {
        redisTemplate.delete(REDIS_KEY);
    }
}
