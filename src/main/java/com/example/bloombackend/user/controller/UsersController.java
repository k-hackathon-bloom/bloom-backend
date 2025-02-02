package com.example.bloombackend.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloombackend.global.config.annotation.CurrentUser;
import com.example.bloombackend.user.controller.dto.request.UserRegisterInfoRequest;
import com.example.bloombackend.user.controller.dto.response.UserInfoResponse;
import com.example.bloombackend.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UsersController {
	private final UserService userService;

	@Autowired
	public UsersController(UserService userService) {
		this.userService = userService;
	}

	@PutMapping
	public ResponseEntity<UserInfoResponse> updateUser(
		@CurrentUser Long userId,
		@RequestBody UserRegisterInfoRequest request) {
		return ResponseEntity.ok(userService.registerUserInfo(userId, request));
	}

	@GetMapping
	public ResponseEntity<UserInfoResponse> getUserInfo(
		@CurrentUser Long userId
	) {
		return ResponseEntity.ok(userService.getUserInfo(userId));
	}

}
