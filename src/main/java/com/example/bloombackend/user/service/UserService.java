package com.example.bloombackend.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.oauth.controller.dto.response.KakaoInfoResponse;
import com.example.bloombackend.user.controller.dto.request.UserRegisterInfoRequest;
import com.example.bloombackend.user.controller.dto.response.UserInfoResponse;
import com.example.bloombackend.user.entity.Age;
import com.example.bloombackend.user.entity.Gender;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Long findOrCreateUser(KakaoInfoResponse response) {
		return userRepository.findBySnsId(response.getProviderId())
			.map(UserEntity::getId)
			.orElseGet(() -> newUser(response));
	}

	private Long newUser(KakaoInfoResponse response) {
		UserEntity user = UserEntity.builder()
			.name(response.getNickname())
			.provider(response.getOAuthProvider())
			.snsId(response.getProviderId())
			.build();

		return userRepository.save(user).getId();
	}

	public UserEntity findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not fount:" + userId));
	}

	@Transactional
	public UserInfoResponse registerUserInfo(Long userId, UserRegisterInfoRequest request) {
		UserEntity user = findUserById(userId);
		user.updateUserSurveyInfo(request.nickname(), Age.valueOf(request.age()),
			Gender.valueOf(request.gender()), request.isSurvey());
		return user.getUserInfo();
	}

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(Long userId) {
		return findUserById(userId).getUserInfo();
	}
}
