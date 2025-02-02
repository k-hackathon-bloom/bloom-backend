package com.example.bloombackend.restdocs;

import com.example.bloombackend.fcm.controller.dto.FcmTokenRegisterRequest;
import com.example.bloombackend.oauth.OAuthProvider;
import com.example.bloombackend.oauth.util.JwtTokenProvider;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class FcmRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    private UserEntity testUser;

    private String mockToken;


    @BeforeEach
    void setUp() {
        mockToken = "jwtToken";
        testUser = userRepository.save(new UserEntity(OAuthProvider.KAKAO, "testUser", "testId"));
        doNothing().when(jwtTokenProvider).validateAccessToken(mockToken);
        doReturn(testUser.getId()).when(jwtTokenProvider).getUserIdFromToken(mockToken);
    }

    @Test
    @DisplayName("API - FCM 토큰 등록")
    void registerFcmToken() throws Exception {
        FcmTokenRegisterRequest request = new FcmTokenRegisterRequest("fcmToken");

        mockMvc.perform(post("/api/fcm/tokens")
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(document("api-fcm-register",
                        requestFields(
                                fieldWithPath("fcmToken").description("등록할 FCM 토큰")
                        )
                ));
    }
}