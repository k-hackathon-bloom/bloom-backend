package com.example.bloombackend.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.bloombackend.oauth.OAuthProvider;
import com.example.bloombackend.user.controller.dto.response.UserInfoResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "sns_id")
	private String snsId;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider", nullable = false)
	private OAuthProvider provider;

	@Column(name = "email")
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "age")
	private Age age;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "is_servey")
	private boolean isSurvey = false;

	@Builder
	public UserEntity(OAuthProvider provider, String name, String snsId) {
		this.provider = provider;
		this.name = name;
		this.snsId = snsId;
	}

	public void updateUserSurveyInfo(String newName, Age age, Gender gender, boolean isSurvey) {
		this.name = newName;
		this.age = age;
		this.gender = gender;
		this.isSurvey = true;
	}

	public UserInfoResponse getUserInfo() {
		return new UserInfoResponse(name, age.toString(), gender.toString(), isSurvey);
	}

	public Long getId() {
		return id;
	}
}
