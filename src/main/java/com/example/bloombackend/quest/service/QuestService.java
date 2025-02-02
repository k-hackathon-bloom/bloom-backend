package com.example.bloombackend.quest.service;

import com.example.bloombackend.global.AIUtil;
import com.example.bloombackend.global.exception.QuestError;
import com.example.bloombackend.quest.controller.dto.QuestRecommendResponse;
import com.example.bloombackend.quest.controller.dto.request.QuestRegisterRequest;
import com.example.bloombackend.quest.controller.dto.response.AvailableQuestsResponse;
import com.example.bloombackend.quest.controller.dto.response.RegisteredQuestsResponse;
import com.example.bloombackend.quest.entity.QuestEntity;
import com.example.bloombackend.quest.entity.UserQuestLogEntity;
import com.example.bloombackend.quest.repository.QuestRepository;
import com.example.bloombackend.quest.repository.UserQuestLogRepository;
import com.example.bloombackend.quest.service.prompt.AiQuestRecommendResponse;
import com.example.bloombackend.quest.service.prompt.QuestRecommendationPrompt;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestService {
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final UserQuestLogRepository userQuestLogRepository;
    private final QuestRecommendationPrompt questRecommendationPrompt;
    private final AIUtil aiUtil;
    private final ObjectMapper objectMapper;

    public QuestService(UserRepository userRepository, QuestRepository questRepository, UserQuestLogRepository userQuestLogRepository, QuestRecommendationPrompt questRecommendationPrompt, AIUtil aiUtil, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.userQuestLogRepository = userQuestLogRepository;
        this.questRecommendationPrompt = questRecommendationPrompt;
        this.aiUtil = aiUtil;
        this.objectMapper = objectMapper;
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

    @Transactional(readOnly = true)
    public QuestRecommendResponse recommendQuests(Long userId) throws JsonProcessingException {
        List<QuestEntity> allQuests = questRepository.findAll();
        List<UserQuestLogEntity> userQuestLogs = userQuestLogRepository.findAllByUser_Id(userId);
        String prompt = questRecommendationPrompt.createPrompt(allQuests, userQuestLogs);
        String aiResponse = aiUtil.generateCompletion(prompt);
        return parseAiResponse(aiResponse);
    }

    private QuestRecommendResponse parseAiResponse(String aiResponse) {
        try {
            return new QuestRecommendResponse(getListFrom(aiResponse));
        } catch (Exception e) {
            return new QuestRecommendResponse(List.of(1L, 2L, 3L));
        }
    }

    private List<Long> getListFrom(String aiResponse) throws JsonProcessingException {
        AiQuestRecommendResponse response = objectMapper.readValue(aiResponse, AiQuestRecommendResponse.class);
        List<Long> recommendedQuestIds = response.recommendedQuestIds();
        if (recommendedQuestIds.size() != 3) throw new JSONException(QuestError.RECOMMEND_QUEST_AI_ERROR.getMessage());
        return recommendedQuestIds;
    }
}