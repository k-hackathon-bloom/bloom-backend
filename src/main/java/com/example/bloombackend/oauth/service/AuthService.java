package com.example.bloombackend.oauth.service;

import com.example.bloombackend.global.exception.RestApiException;
import com.example.bloombackend.global.exception.TokenError;
import com.example.bloombackend.oauth.util.JwtTokenProvider;
import com.example.bloombackend.oauth.controller.dto.request.LogoutRequest;
import com.example.bloombackend.oauth.controller.dto.request.RefreshRequest;
import com.example.bloombackend.oauth.controller.dto.response.LoginResponse;
import com.example.bloombackend.oauth.entity.LogoutList;
import com.example.bloombackend.oauth.repository.LogoutListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bloombackend.oauth.controller.dto.response.KakaoInfoResponse;
import com.example.bloombackend.user.service.UserService;

import java.time.LocalDateTime;

@Service
public class AuthService {
	private final RequestKakaoInfoService requestKakaoInfoService;
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final LogoutListRepository logoutListRepository;

	@Autowired
	private AuthService(RequestKakaoInfoService requestKakaoInfoService, UserService userService, JwtTokenProvider jwtTokenProvider, LogoutListRepository logoutListRepository) {
		this.requestKakaoInfoService = requestKakaoInfoService;
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.logoutListRepository = logoutListRepository;
	}

	public LoginResponse login(String authorizationCode) {
		KakaoInfoResponse response = requestKakaoInfoService.request(authorizationCode);
		Long userId = userService.findOrCreateUser(response);
		String accessToken = jwtTokenProvider.createAccessToken(userId.toString());
		String refreshToken = jwtTokenProvider.createRefreshToken(userId.toString());
		return new LoginResponse(accessToken, refreshToken);
	}

	public String getKakaoLoginUrl() {
		return requestKakaoInfoService.getRedirectUri();
	}

	public String refreshAccessToken(RefreshRequest request) {
		if (isTokenLoggedOut(request.refreshToken())) {
			throw new RestApiException(TokenError.LOGGED_OUT_REFRESH_TOKEN);
		}
		return jwtTokenProvider.refreshAccessToken(request.refreshToken());
	}

	public void logout(LogoutRequest request) {
		if (isTokenLoggedOut(request.refreshToken())) {
			throw new RestApiException(TokenError.LOGGED_OUT_REFRESH_TOKEN);
		}
		addToLogoutList(request.refreshToken());
	}

	private void addToLogoutList(String refreshToken) {
		String tokenHash = jwtTokenProvider.hashToken(refreshToken);
		LocalDateTime expirationTime = jwtTokenProvider.getExpirationTime(refreshToken);
		LogoutList logoutList = new LogoutList(tokenHash, expirationTime);
		logoutListRepository.save(logoutList);
	}

	private boolean isTokenLoggedOut(String token) {
		String tokenHash = jwtTokenProvider.hashToken(token);
		return logoutListRepository.findByTokenHash(tokenHash).isPresent();
	}

	private void removeExpiredTokens() {
		LocalDateTime now = LocalDateTime.now();
		logoutListRepository.deleteByExpirationTimeBefore(now);
	}
}