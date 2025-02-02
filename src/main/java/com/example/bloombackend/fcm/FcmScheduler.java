package com.example.bloombackend.fcm;

import com.example.bloombackend.quest.service.QuestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FcmScheduler {
    private final QuestService questService;

    public FcmScheduler(QuestService questService) {
        this.questService = questService;
    }

    @Scheduled(cron = "0 0 13,19 * * ?") // 매일 1시, 19시에 실행
    public void scheduleDailyNotifications() {
        questService.sendDailyQuestNotifications();
    }
}