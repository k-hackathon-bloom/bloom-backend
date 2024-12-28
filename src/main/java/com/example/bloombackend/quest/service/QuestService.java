package com.example.bloombackend.quest.service;

import com.example.bloombackend.global.FcmUtil;
import com.example.bloombackend.quest.controller.dto.request.QuestRegisterRequest;
import com.example.bloombackend.quest.controller.dto.response.AvailableQuestsResponse;
import com.example.bloombackend.quest.controller.dto.response.RegisteredQuestsResponse;
import com.example.bloombackend.quest.entity.QuestEntity;
import com.example.bloombackend.quest.entity.UserQuestLogEntity;
import com.example.bloombackend.quest.repository.QuestRepository;
import com.example.bloombackend.quest.repository.UserQuestLogRepository;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestService {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final UserQuestLogRepository userQuestLogRepository;
    private final FcmUtil fcmUtil;

    public QuestService(UserRepository userRepository, QuestRepository questRepository, UserQuestLogRepository userQuestLogRepository, FcmUtil fcmUtil) {
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.userQuestLogRepository = userQuestLogRepository;
        this.fcmUtil = fcmUtil;
    }

    @Transactional(readOnly = true)
    public AvailableQuestsResponse getAvailableQuests() {
        return new AvailableQuestsResponse(questRepository.findAll().stream()
                .map(QuestEntity::toDto)
                .toList());
    }

    @Transactional
    public void registerQuests(Long userId, QuestRegisterRequest request) {
        UserEntity user = getUser(userId);
        userQuestLogRepository.saveAll(createUserQuestLogEntities(request, user));
    }

    private List<UserQuestLogEntity> createUserQuestLogEntities(QuestRegisterRequest request, UserEntity user) {
        List<QuestEntity> unLoggedQuests = questRepository.findUnLoggedQuests(user, request.questIds());
        return unLoggedQuests.stream()
                .map(quest -> new UserQuestLogEntity(user, quest))
                .toList();
    }

    private UserEntity getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    @Transactional(readOnly = true)
    public RegisteredQuestsResponse getRegisteredQuestsForToday(Long userId) {
        List<UserQuestLogEntity> logsForToday = userQuestLogRepository.findAllByUserIdAndSelectedDateBetween(userId, getStartOfDay(), getEndOfDay());
        return new RegisteredQuestsResponse(logsForToday.stream()
                .map(UserQuestLogEntity::toDto)
                .toList());
    }

    private LocalDateTime getStartOfDay() {
        return LocalDateTime.now().toLocalDate().atStartOfDay();
    }

    private LocalDateTime getEndOfDay() {
        return LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
    }

    @Transactional
    public void unregisterQuest(Long userId, Long questId) {
        userQuestLogRepository.deleteByUserIdAndQuestId(userId, questId);
    }

    @Transactional
    public void completeQuest(Long userId, Long questId) {
         userQuestLogRepository.completeQuest(userId, questId);
    }

    @Transactional
    public void sendDailyQuestNotifications() {
        List<UserQuestLogEntity> incompleteQuests = userQuestLogRepository.findIncompleteQuestsByDate(LocalDate.now());

        Map<UserEntity, List<UserQuestLogEntity>> userQuestMap = incompleteQuests.stream()
                .collect(Collectors.groupingBy(UserQuestLogEntity::getUser));

        userQuestMap.forEach((user, quests) -> {
            String title = "\uD83D\uDCAA 오늘의 퀘스트 알림!";
            String body = quests.stream()
                    .map(q -> q.getQuest().getTitle())
                    .collect(Collectors.joining(", ")) + "를 완료해보세요!";
            try {
                fcmUtil.sendNotification(user.getFcmToken(), title, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}