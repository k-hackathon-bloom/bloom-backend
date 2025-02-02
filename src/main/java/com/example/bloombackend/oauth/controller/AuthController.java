package com.example.bloombackend.oauth.controller;

import com.example.bloombackend.oauth.controller.dto.request.LogoutRequest;
import com.example.bloombackend.oauth.controller.dto.request.RefreshRequest;
import com.example.bloombackend.oauth.controller.dto.response.RefreshResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloombackend.oauth.controller.dto.request.KakaoLoginRequest;
import com.example.bloombackend.oauth.controller.dto.response.LoginResponse;
import com.example.bloombackend.oauth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/kakao")
	public ResponseEntity<LoginResponse> loginKakao(@RequestBody KakaoLoginRequest request) {
		return ResponseEntity.ok(authService.login(request.authorizationCode()));
	}

	@GetMapping("/kakao/login")
	public String redirectToKakaoLogin() {
		return authService.getKakaoLoginUrl();
	}

	@PostMapping("/refresh")
	public ResponseEntity<RefreshResponse> refreshAccessToken(@RequestBody RefreshRequest request) {
		String accessToken = authService.refreshAccessToken(request);
		return ResponseEntity.ok(new RefreshResponse(accessToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
		authService.logout(request);
		return ResponseEntity.ok("로그아웃되었습니다.");
	}
}