package com.example.bloombackend.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloombackend.global.config.annotation.CurrentUser;
import com.example.bloombackend.user.controller.dto.request.UserRegisterInfoRequest;
import com.example.bloombackend.user.controller.dto.response.UserInfoResponse;
import com.example.bloombackend.user.service.UserService;

@RestController
@RequestMapping("/api/users")
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

}
