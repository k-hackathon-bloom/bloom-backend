package com.example.bloombackend.restdocs;

import com.example.bloombackend.global.exception.TokenError;
import com.example.bloombackend.oauth.controller.dto.request.KakaoLoginRequest;
import com.example.bloombackend.oauth.controller.dto.request.LogoutRequest;
import com.example.bloombackend.oauth.controller.dto.request.RefreshRequest;
import com.example.bloombackend.oauth.entity.LogoutList;
import com.example.bloombackend.oauth.repository.LogoutListRepository;
import com.example.bloombackend.oauth.service.RequestKakaoInfoService;
import com.example.bloombackend.oauth.util.JwtTokenProvider;
import com.example.bloombackend.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AuthRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private RequestKakaoInfoService requestKakaoInfoService;

    @SpyBean
    private UserService userService;

    @SpyBean
    private LogoutListRepository logoutListRepository;

    @Test
    @DisplayName("API - 카카오 로그인 URL 조회")
    void getKakaoLoginUrl() throws Exception {
        mockMvc.perform(get("/api/auth/kakao/login"))
                .andExpect(status().isOk())
                .andDo(document("api-auth-test/kakao-login-url"));
    }

    @Test
    @DisplayName("API - 로그인")
    void login() throws Exception {
        KakaoLoginRequest request = new KakaoLoginRequest("authorization_code_example");
        doReturn(null).when(requestKakaoInfoService).request("authorization_code_example");
        doReturn(1L).when(userService).findOrCreateUser(any());

        mockMvc.perform(post("/api/auth/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("api-auth-test/kakao-login",
                        requestFields(
                                fieldWithPath("authorizationCode").description("카카오 로그인 인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("발급된 Access 토큰"),
                                fieldWithPath("refreshToken").description("발급된 Refresh 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("API - Access 토큰 재발급")
    void refreshAccessToken() throws Exception {
        String refreshToken = jwtTokenProvider.createRefreshToken("1");
        RefreshRequest request = new RefreshRequest(refreshToken);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("api-auth-test/refresh-token",
                        requestFields(
                                fieldWithPath("refreshToken").description("Refresh 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("재발급된 Access 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("API - 로그아웃")
    void logout() throws Exception {
        String refreshToken = jwtTokenProvider.createRefreshToken("2");
        LogoutRequest request = new LogoutRequest(refreshToken);

        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, refreshToken))
                .andExpect(status().isOk())
                .andDo(document("api-auth-test/logout",
                        requestFields(
                                fieldWithPath("refreshToken").description("Refresh 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("Exception - 로그아웃된 Refresh 토큰으로 Access 토큰 재발급 시도 시 예외 발생")
    void refreshAccessTokenWithLoggedOutTokenTest() throws Exception {
        String refreshToken = jwtTokenProvider.createRefreshToken("3");
        LogoutList logoutList = new LogoutList(
                                        jwtTokenProvider.hashToken(refreshToken),
                                        jwtTokenProvider.getExpirationTime(refreshToken)
                                    );
        doReturn(Optional.of(logoutList)).when(logoutListRepository).findByTokenHash(any());


        LogoutRequest request = new LogoutRequest(refreshToken);
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(TokenError.LOGGED_OUT_REFRESH_TOKEN.getDevelopCode()))
                .andExpect(jsonPath("$.message").value(TokenError.LOGGED_OUT_REFRESH_TOKEN.getMessage()))
                .andDo(document("api-auth-test/logged-out-token",
                        responseFields(
                                fieldWithPath("errorCode").description(TokenError.LOGGED_OUT_REFRESH_TOKEN.getDevelopCode()),
                                fieldWithPath("message").description(TokenError.LOGGED_OUT_REFRESH_TOKEN.getMessage())
                        )
                ));
    }

    @Test
    @DisplayName("Exception - 만료된 refresh 토큰으로 Access 토큰 재발급 시도 시 예외 발생")
    void refreshAccessTokenWithExpiredRefreshTokenTest() throws Exception {
        String expiredToken = createExpiredToken();
        RefreshRequest request = new RefreshRequest(expiredToken);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(TokenError.EXPIRED_REFRESH_TOKEN.getDevelopCode()))
                .andExpect(jsonPath("$.message").value(TokenError.EXPIRED_REFRESH_TOKEN.getMessage()))
                .andDo(document("api-auth-test/expired-refresh-token",
                        responseFields(
                                fieldWithPath("errorCode").description(TokenError.EXPIRED_REFRESH_TOKEN.getDevelopCode()),
                                fieldWithPath("message").description(TokenError.EXPIRED_REFRESH_TOKEN.getMessage())
                        )
                ));
    }

    @Test
    @DisplayName("Exception - 유효하지 않은 Refresh 토큰으로 Access 토큰 재발급 시도 시 예외 발생")
    void refreshAccessTokenWithInvalidRefreshTokenTest() throws Exception {
        String invalidToken = "잘못된.토큰.값";
        RefreshRequest request = new RefreshRequest(invalidToken);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, invalidToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(TokenError.INVALID_REFRESH_TOKEN.getDevelopCode()))
                .andExpect(jsonPath("$.message").value(TokenError.INVALID_REFRESH_TOKEN.getMessage()))
                .andDo(document("api-auth-test/invalid-refresh-token",
                        responseFields(
                                fieldWithPath("errorCode").description(TokenError.INVALID_REFRESH_TOKEN.getDevelopCode()),
                                fieldWithPath("message").description(TokenError.INVALID_REFRESH_TOKEN.getMessage())
                        )
                ));

    }

            @Test
    @DisplayName("Exception - 유효하지 않은 Access 토큰으로 유저 정보 조회 시 예외 발생")
    void getUserInfoWithInvalidTokenTest() throws Exception {
        String invalidToken = "잘못된.토큰.값";

        mockMvc.perform(get("/api/user")
                        .header(HttpHeaders.AUTHORIZATION, invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(TokenError.INVALID_ACCESS_TOKEN.getDevelopCode()))
                .andExpect(jsonPath("$.message").value(TokenError.INVALID_ACCESS_TOKEN.getMessage()))
                .andDo(document("api-auth-test/invalid-token",
                        responseFields(
                                fieldWithPath("errorCode").description(TokenError.INVALID_ACCESS_TOKEN.getDevelopCode()),
                                fieldWithPath("message").description(TokenError.INVALID_ACCESS_TOKEN.getMessage())
                        )
                ));
    }

    @Test
    @DisplayName("Exception - 만료된 Access 토큰으로 유저 정보 조회 시 예외 발생")
    void getUserInfoWithExpiredTokenTest() throws Exception {
        String expiredToken = createExpiredToken();

        mockMvc.perform(get("/api/user")
                        .header(HttpHeaders.AUTHORIZATION, expiredToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(TokenError.EXPIRED_ACCESS_TOKEN.getDevelopCode()))
                .andExpect(jsonPath("$.message").value(TokenError.EXPIRED_ACCESS_TOKEN.getMessage()))
                .andDo(document("api-auth-test/expired-token",
                        responseFields(
                                fieldWithPath("errorCode").description(TokenError.EXPIRED_ACCESS_TOKEN.getDevelopCode()),
                                fieldWithPath("message").description(TokenError.EXPIRED_ACCESS_TOKEN.getMessage())
                        )
                ));
    }

    private String createExpiredToken() throws Exception {
        long originalValue = updateValidTime(0L);
        String expiredToken = jwtTokenProvider.createAccessToken("1");
        updateValidTime(originalValue);
        return expiredToken;
    }

    private long updateValidTime(long ValidSeconds) throws Exception {
        Field field = JwtTokenProvider.class.getDeclaredField("accessTokenValidSeconds");
        field.setAccessible(true);
        long originalValue = (long) field.get(jwtTokenProvider);
        field.set(jwtTokenProvider, ValidSeconds);
        return originalValue;
    }
}