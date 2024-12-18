package com.example.bloombackend.achievement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.achievement.controller.dto.request.AchievementLevelUpdateRequest;
import com.example.bloombackend.achievement.controller.dto.request.FlowerRegisterRequest;
import com.example.bloombackend.achievement.controller.dto.response.AchievementLevelUpdateResponse;
import com.example.bloombackend.achievement.controller.dto.response.DailyAchievementResponse;
import com.example.bloombackend.achievement.controller.dto.response.MonthlyAchievementResponse;
import com.example.bloombackend.achievement.controller.dto.response.MonthlyDataResponse;
import com.example.bloombackend.achievement.controller.dto.response.RecentSixMonthDataResponse;
import com.example.bloombackend.achievement.controller.response.DailyFlowerResponse;
import com.example.bloombackend.achievement.entity.DailyAchievementEntity;
import com.example.bloombackend.achievement.repository.DailyAchievementRepository;
import com.example.bloombackend.achievement.service.prompt.AchievementAIPrompt;
import com.example.bloombackend.global.AIUtil;
import com.example.bloombackend.item.entity.items.SeedEntity;
import com.example.bloombackend.item.repository.SeedRepository;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AchievementService {

	private final DailyAchievementRepository dailyAchievementRepository;
	private final SeedRepository seedRepository;
	private final UserRepository userRepository;
	private final AIUtil aiUtil;
	private final ObjectMapper objectMapper;

	public AchievementService(DailyAchievementRepository dailyAchievementRepository, SeedRepository seedRepository,
		UserRepository userRepository, AIUtil aiUtil, ObjectMapper objectMapper) {
		this.dailyAchievementRepository = dailyAchievementRepository;
		this.seedRepository = seedRepository;
		this.userRepository = userRepository;
		this.aiUtil = aiUtil;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public void setDailyFlower(Long userId, FlowerRegisterRequest request) {
		UserEntity user = getUserEntity(userId);
		SeedEntity flower = getFlowerEntity(request);
		if (isFlowerRegistered(userId)) {
			throw new IllegalArgumentException("flower already registered for today");
		}
		dailyAchievementRepository.save(new DailyAchievementEntity(user, flower));
	}

	private boolean isFlowerRegistered(Long userId) {
		LocalDate now = LocalDate.now();
		LocalDateTime startOfToday = now.atStartOfDay();
		LocalDateTime endOfToday = now.atTime(LocalTime.MAX);
		return dailyAchievementRepository.existsByUserIdAndCreatedAtBetween(userId, startOfToday, endOfToday);
	}

	private SeedEntity getFlowerEntity(FlowerRegisterRequest request) {
		return seedRepository.findById(request.flowerId())
			.orElseThrow(() -> new EntityNotFoundException("Flower not found:" + request.flowerId()));
	}

	private UserEntity getUserEntity(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found:" + userId));
	}

	@Transactional
	public AchievementLevelUpdateResponse updateAchievementLevel(Long userId, AchievementLevelUpdateRequest request) {
		DailyAchievementEntity dailyAchievement = getDailyAchievementEntity(userId);
		dailyAchievement.increaseAchievementLevel(request.increaseBy());
		dailyAchievementRepository.save(dailyAchievement);
		return new AchievementLevelUpdateResponse(dailyAchievement.getAchievementLevel());
	}

	@Transactional(readOnly = true)
	public MonthlyDataResponse getMonthlyAchievements(Long userId, String month) {
		List<DailyAchievementResponse> dailyData = getMonthlyAchievementEntities(userId, month).stream()
			.map(DailyAchievementEntity::toDto)
			.toList();
		return new MonthlyDataResponse(dailyData);
	}

	private DailyAchievementEntity getDailyAchievementEntity(Long userId) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
		return dailyAchievementRepository.findFirstByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay)
			.orElseThrow(() -> new EntityNotFoundException("Daily achievement not found for user:" + userId));
	}

	private List<DailyAchievementEntity> getMonthlyAchievementEntities(Long userId, String month) {
		LocalDateTime startOfMonth = LocalDate.parse(month + "-01").atStartOfDay();
		LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
		return dailyAchievementRepository.findByUserIdAndCreatedAtBetween(userId, startOfMonth, endOfMonth);
	}

	@Transactional(readOnly = true)
	public RecentSixMonthDataResponse getRecentSixMonthsAchievements(Long userId) {
		List<MonthlyAchievementResponse> monthlyAchievements = getMonthlyAchievements(userId);
		double averageBloomed = calculateAverageBloomed(monthlyAchievements);
		String summary = generateAchievementSummary(monthlyAchievements, averageBloomed);
		return new RecentSixMonthDataResponse(monthlyAchievements, averageBloomed, summary);
	}

	private List<MonthlyAchievementResponse> getMonthlyAchievements(Long userId) {
		return dailyAchievementRepository.getRecentSixMonthsAchievements(userId);
	}

	private double calculateAverageBloomed(List<MonthlyAchievementResponse> monthlyAchievements) {
		return monthlyAchievements.stream()
			.mapToInt(MonthlyAchievementResponse::bloomed)
			.average()
			.orElse(0);
	}

	private String generateAchievementSummary(List<MonthlyAchievementResponse> monthlyAchievements,
		double averageBloomed) {
		String monthlyData = serializeMonthlyAchievements(monthlyAchievements);
		String prompt = createAIPrompt(monthlyData, averageBloomed);
		return aiUtil.generateCompletion(prompt);
	}

	private String serializeMonthlyAchievements(List<MonthlyAchievementResponse> monthlyAchievements) {
		try {
			return objectMapper.writeValueAsString(monthlyAchievements);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private String createAIPrompt(String monthlyData, double averageBloomed) {
		return String.format(AchievementAIPrompt.ACHIEVEMENT_SUMMARY_PROMPT, monthlyData, averageBloomed);
	}

	@Transactional(readOnly = true)
	public DailyFlowerResponse getDailyFlower(Long userId) {
		return dailyAchievementRepository.getDailyFlower(userId);
	}

	@Transactional(readOnly = true)
	public DailyAchievementResponse getDailyAchievement(final Long userId) {
		DailyAchievementEntity dailyAchievement = getDailyAchievementEntity(userId);
		return dailyAchievement.toDto();
	}
}
