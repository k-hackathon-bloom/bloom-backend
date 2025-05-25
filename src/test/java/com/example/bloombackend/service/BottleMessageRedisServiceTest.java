package com.example.bloombackend.service;

import com.example.bloombackend.bottlemsg.service.BottleMessageRedisService;
import com.example.bloombackend.bottlemsg.service.BottleMessageService;
import com.example.bloombackend.bottlemsg.utill.BottleMsgScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class BottleMessageRedisServiceTest {
    @Autowired
    private BottleMessageRedisService redisService;

    @Autowired
    private BottleMsgScheduler scheduler;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private BottleMessageService messageService;

    private final String redisKey = "bottleMessage:senders";

    @BeforeEach
    void setUp() {
        redisTemplate.delete(redisKey);
        redisService.clearSenders(); // 초기화
    }

    @Test
    void addSender_shouldAddUserIdToSet() {
        // given
        Long userId = 123L;

        // when
        redisService.addSender(userId);

        // then
        Set<String> members = redisService.getAllSenders();

        assertThat(members).contains(String.valueOf(userId));
    }

    @Test
    void getAllSenders_shouldReturnAllUserIds() {
        // given
        redisService.addSender(111L);
        redisService.addSender(222L);

        // when
        Set<String> allSenders = redisService.getAllSenders();

        // then
        assertThat(allSenders).containsExactlyInAnyOrder("111", "222");
    }

    @Test
    void clearSenders_shouldDeleteTheRedisKey() {
        // given
        redisService.addSender(333L);

        // when
        redisService.clearSenders();

        // then
        Set<String> senders = redisService.getAllSenders();
        assertThat(senders).isEmpty();
    }

    @Test
    void sendMessages_shouldSendMessagesToStoredUsers() {
        // given
        redisService.addSender(100L);
        redisService.addSender(200L);

        // when
        scheduler.sendMessages();

        // then
        verify(messageService).getRandomBottleMessage(100L);
        verify(messageService).getRandomBottleMessage(200L);

        // Redis 비워졌는지 확인
        Set<String> remaining = redisService.getAllSenders();
        assertThat(remaining).isEmpty();
    }
}