package com.example.bloombackend.achievement.entity;

import java.time.LocalDateTime;

import com.example.bloombackend.achievement.controller.dto.response.DailyAchievementResponse;
import com.example.bloombackend.item.entity.items.SeedEntity;
import com.example.bloombackend.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_achievement")
public class DailyAchievementEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flower_id", nullable = false)
	private SeedEntity flower;

	@Column(name = "achievement_level", nullable = false)
	private int achievementLevel = 0;

	@Column(updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public DailyAchievementEntity(UserEntity user, SeedEntity flower) {
		this.user = user;
		this.flower = flower;
	}

	public int getAchievementLevel() {
		return achievementLevel;
	}

	public void increaseAchievementLevel(int increaseBy) {
		if (achievementLevel + increaseBy >= 0 && achievementLevel + increaseBy <= 9) {
			achievementLevel += increaseBy;
		}
	}

	public DailyAchievementResponse toDto() {
		return new DailyAchievementResponse(createdAt.toLocalDate(), getIconUrl(), achievementLevel);
	}

	private String getIconUrl() {
		return achievementLevel == 9 ? flower.getBigIconUrl() : AchievementIcon.fromLevel(achievementLevel).getUrl();
	}
}
