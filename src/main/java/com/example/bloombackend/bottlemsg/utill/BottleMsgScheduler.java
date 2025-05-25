package com.example.bloombackend.bottlemsg.utill;

import com.example.bloombackend.bottlemsg.service.BottleMessageRedisService;
import com.example.bloombackend.bottlemsg.service.BottleMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BottleMsgScheduler {
    private final BottleMessageRedisService redisService;
    private final BottleMessageService messageService;

    public BottleMsgScheduler(BottleMessageRedisService redisService, BottleMessageService messageService) {
        this.redisService = redisService;
        this.messageService = messageService;
    }

    @Scheduled(cron = "0 0 10,14,18,22 * * *")
    public void scheduledSend() {
        sendMessages();
    }

    public void sendMessages() {
        Set<String> userIds = redisService.getAllSenders();
        if (userIds == null || userIds.isEmpty()) return;

        for (String userIdStr : userIds) {
            Long userId = Long.valueOf(userIdStr);
            messageService.getRandomBottleMessage(userId);
        }

        redisService.clearSenders();
    }
}
