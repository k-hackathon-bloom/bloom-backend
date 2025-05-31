package com.example.bloombackend.restdocs;

import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageReactionRequest;
import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageRequest;
import com.example.bloombackend.bottlemsg.entity.*;
import com.example.bloombackend.bottlemsg.repository.BottleMessageLogRepository;
import com.example.bloombackend.bottlemsg.repository.BottleMessageRepository;
import com.example.bloombackend.bottlemsg.repository.BottleMessageSentLogRepository;
import com.example.bloombackend.bottlemsg.repository.PostcardRepository;
import com.example.bloombackend.global.AIUtil;
import com.example.bloombackend.oauth.OAuthProvider;
import com.example.bloombackend.oauth.util.JwtTokenProvider;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class BottleMessageRestDocsTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BottleMessageRepository bottleMessageRepository;

	@Autowired
	private BottleMessageLogRepository bottleMessageLogRepository;

	@Autowired
	private BottleMessageSentLogRepository bottleMessageSentLogRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostcardRepository postcardRepository;

	@SpyBean
	private JwtTokenProvider jwtTokenProvider;

	@SpyBean
	private AIUtil aiUtil;

	private UserEntity testUser;

	private UserEntity testSender;

	private String mockToken;

	private ObjectMapper objectMapper;

	private BottleMessageEntity bottleMessage1;

	private BottleMessageEntity bottleMessage2;

	private BottleMessageSentLog bottleMessageSentLog1;

	private BottleMessageSentLog bottleMessageSentLog2;

	private PostcardEntity postcard1;

	@Autowired
	private ParameterNamesModule parameterNamesModule;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockToken = "jwtToken";
		testUser = userRepository.save(new UserEntity(OAuthProvider.KAKAO, "testUser", "testId"));
		doNothing().when(jwtTokenProvider).validateAccessToken(mockToken);
		doReturn(testUser.getId()).when(jwtTokenProvider).getUserIdFromToken(mockToken);
		testSender = userRepository.save(new UserEntity(OAuthProvider.KAKAO, "testSender", "testSenderId"));

		String messageAnalysis = "| 관련된 감정 | 퍼센트 |\n" +
				"|-------------|--------|\n" +
				"| 우울함      | 70     |\n" +
				"| 외로움      | 60     |\n" +
				"| 불안        | 50     |\n" +
				"| 부정적 영향여부 |\n" +
				"| ------------ |\n" +
				"| UPPER       |";

		// 외부 API 호출의 경우 테스트 시 Mocking 처리
		doReturn(messageAnalysis).when(aiUtil).generateCompletion(anyString());

		postcard1 = postcardRepository.save(
				PostcardEntity.builder()
						.postcardUrl("https://domain/bottle-message-poster/letter-paper-1.jpg").build()
		);

		bottleMessage1 = bottleMessageRepository.save(
			BottleMessageEntity.builder()
				.user(testSender)
				.title("내일은 또 다른날")
				.content("오늘은 금요일 내일은 토요일")
				.postcard(postcard1)
				.build()
		);
		bottleMessage2 = bottleMessageRepository.save(
			BottleMessageEntity.builder()
				.user(testSender)
				.title("모두모두 화이팅")
				.content("모두 모두 화이팅")
				.postcard(postcard1)
				.build()
		);

		bottleMessageSentLog1 = bottleMessageSentLogRepository.save(
				BottleMessageSentLog.builder()
						.message(bottleMessage1)
						.senderId(testSender.getId())
						.build()
		);

		bottleMessageSentLog2 = bottleMessageSentLogRepository.save(
				BottleMessageSentLog.builder()
						.message(bottleMessage2)
						.senderId(testSender.getId())
						.build()
		);
	}

	@Test
	@DisplayName("API - 유리병 메시지 등록")
	void createBottleMessageTest() throws Exception {
		//given
		CreateBottleMessageRequest request = new CreateBottleMessageRequest("힘든하루였지만", "보람차",postcard1.getId());

		//when & then
		mockMvc.perform(post("/api/bottle-messages")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/post-bottle-message",
				requestFields(
					fieldWithPath("title").description("등록할 유리병 메시지 제목"),
					fieldWithPath("content").description("등록할 유리병 메시지 내용"),
					fieldWithPath("postcardId").description("선택한 편지지 ID")
				),
				responseFields(
					fieldWithPath("id").description("등록한 유리병 메시지 아이디")
				)
			));
	}

	@Test
	@DisplayName("API - 유저 유리병 메시지 목록 조회")
	void getUserBottleMessages() throws Exception {
		//given
		BottleMessageReceiptLog log1 = BottleMessageReceiptLog.builder()
			.recipient(testUser)
			.message(bottleMessage1)
			.build();
		BottleMessageReceiptLog log2 = BottleMessageReceiptLog.builder()
			.recipient(testUser)
			.message(bottleMessage2)
			.build();
		bottleMessageLogRepository.saveAll(List.of(log1, log2));

		//when & then
		mockMvc.perform(get("/api/bottle-messages")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/get-bottle-messages",
				responseFields(
					fieldWithPath("bottleMessageResponses[]").description("유리병 메시지 목록"),
					fieldWithPath("bottleMessageResponses[].log").description("유리병 메시지 수발신 로그 정보"),
					fieldWithPath("bottleMessageResponses[].log.receivedAt").description("수신일시"),
					fieldWithPath("bottleMessageResponses[].log.sentAt").description("발신일시"),
					fieldWithPath("bottleMessageResponses[].messages").description("메시지 정보"),
					fieldWithPath("bottleMessageResponses[].messages.messageId").description("유리병 메시지 아이디"),
					fieldWithPath("bottleMessageResponses[].messages.title").description("유리병 메시지 제목"),
					fieldWithPath("bottleMessageResponses[].messages.postCardUrl").description("유리병 메시지 편지지 url"),
					fieldWithPath("bottleMessageResponses[].messages.negativity").description("유리병 메시지 위험도")
				)
			));
	}

	@Test
	@DisplayName("API - 유리병 메시지 상세 조회 조회")
	void getBottleMessage() throws Exception {
		//when & then
		mockMvc.perform(get("/api/bottle-messages/{messageId}", bottleMessage1.getId())
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/get-bottle-message",
				pathParameters(
					parameterWithName("messageId").description("조회할 메시지 아이디")
				),
				responseFields(
					fieldWithPath("message").description("유리병 메시지 정보"),
					fieldWithPath("message.messageId").description("유리병 메시지 아이디"),
					fieldWithPath("message.title").description("유리병 메시지 제목"),
					fieldWithPath("message.content").description("유리병 메시지 내용"),
					fieldWithPath("message.postCardUrl").description("유리병 메시지 편지지 Url"),
					fieldWithPath("message.negativity").description("유리병 메시지 위험도"),
					fieldWithPath("reaction").description("유리병 메시지 반응 정보"),
					fieldWithPath("reaction.isReacted").description("해당 유리병 메시지 유저 반응 여부"),
					fieldWithPath("reaction.like").description("유리병 메시지 좋아요 개수"),
					fieldWithPath("reaction.empathy").description("유리병 메시지 공감해요 개수"),
					fieldWithPath("reaction.cheer").description("유리병 메시지 응원해요 개수")
				)
			));
	}

	@Test
	@DisplayName("API - 유리병 메시지 랜덤 조회")
	void getRandomBottleMessage() throws Exception {
		//given
		bottleMessage1.updateNegativity(Negativity.LOWER);
		bottleMessage2.updateNegativity(Negativity.LOWER);

		//when & then
		mockMvc.perform(get("/api/bottle-messages/random")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/get-random-bottle-message",
				responseFields(
					fieldWithPath("message").description("유리병 메시지 정보"),
					fieldWithPath("message.messageId").description("유리병 메시지 아이디"),
					fieldWithPath("message.title").description("유리병 메시지 제목"),
					fieldWithPath("message.content").description("유리병 메시지 내용"),
					fieldWithPath("message.postCardUrl").description("유리병 메시지 편지지 Url"),
					fieldWithPath("message.negativity").description("유리병 메시지 위험도").optional(),
					fieldWithPath("reaction").description("유리병 메시지 반응 정보"),
					fieldWithPath("reaction.isReacted").description("해당 유리병 메시지 유저 반응 여부"),
					fieldWithPath("reaction.like").description("유리병 메시지 좋아요 개수"),
					fieldWithPath("reaction.empathy").description("유리병 메시지 공감해요 개수"),
					fieldWithPath("reaction.cheer").description("유리병 메시지 응원해요 개수")
				)
			));
	}

	@Test
	@DisplayName("API - 유리병 메시지 반응 등록")
	void postBottleMessageReaction() throws Exception {
		//given
		CreateBottleMessageReactionRequest request = new CreateBottleMessageReactionRequest("LIKE");

		//when & then
		mockMvc.perform(post("/api/bottle-messages/{messageId}/react", bottleMessage1.getId())
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/post-bottle-message-reaction",
				pathParameters(
					parameterWithName("messageId").description("조회할 메시지 아이디")
				),
				requestFields(
					fieldWithPath("reaction").description("등록할 반응 (LIKE,CHEER,EMPATHY)")
				),
				responseFields(
					fieldWithPath("isReacted").description("유저의 반응여부"),
					fieldWithPath("like").description("좋아요 개수"),
					fieldWithPath("empathy").description("공감해요 개수"),
					fieldWithPath("cheer").description("응원해요 개수")
				)
			));
	}

	@Test
	@DisplayName("API - 받은 유리병 메시지 삭제")
	void deleteBottleMessage() throws Exception {
		//given
		BottleMessageReceiptLog log1 = BottleMessageReceiptLog.builder()
			.recipient(testUser)
			.message(bottleMessage1)
			.build();
		BottleMessageReceiptLog log2 = BottleMessageReceiptLog.builder()
			.recipient(testUser)
			.message(bottleMessage2)
			.build();
		bottleMessageLogRepository.saveAll(List.of(log1, log2));

		//when & then
		mockMvc.perform(post("/api/bottle-messages/{messageId}/delete", bottleMessage1.getId())
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/delete-bottle-message",
				pathParameters(
					parameterWithName("messageId").description("삭제할 메시지 아이디")
				),
				responseFields(
					fieldWithPath("bottleMessageResponses[]").description("유리병 메시지 목록"),
					fieldWithPath("bottleMessageResponses[].log").description("유리병 메시지 수발신 로그 정보"),
					fieldWithPath("bottleMessageResponses[].log.receivedAt").description("수신일시"),
					fieldWithPath("bottleMessageResponses[].log.sentAt").description("발신일시"),
					fieldWithPath("bottleMessageResponses[].messages").description("메시지 정보"),
					fieldWithPath("bottleMessageResponses[].messages.messageId").description("유리병 메시지 아이디"),
					fieldWithPath("bottleMessageResponses[].messages.title").description("유리병 메시지 제목"),
					fieldWithPath("bottleMessageResponses[].messages.postCardUrl").description("유리병 메시지 편지지 url"),
					fieldWithPath("bottleMessageResponses[].messages.negativity").description("유리병 메시지 위험도")
				)
			));
	}

	@Test
	@DisplayName("API - 보낸 유리병 메시지 목록 조회")
	void getSentBottleMessage() throws Exception {
		//given
		BottleMessageSentLog log1 = BottleMessageSentLog.builder()
				.senderId(testUser.getId())
				.message(bottleMessage1)
				.build();

		BottleMessageSentLog log2 = BottleMessageSentLog.builder()
				.senderId(testUser.getId())
				.message(bottleMessage2)
				.build();

		bottleMessageSentLogRepository.saveAll(List.of(log1, log2));
		bottleMessage1.updateNegativity(Negativity.LOWER);
		bottleMessage2.updateNegativity(Negativity.UPPER);

		//when & then
		mockMvc.perform(get("/api/bottle-messages/sent")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/get-sent-bottle-messages",
				responseFields(
					fieldWithPath("messages[]").description("유리병 메시지 목록"),
					fieldWithPath("messages[].sentAt").description("보낸 시간"),
					fieldWithPath("messages[].message.messageId").description("유리병 메시지 아이디"),
					fieldWithPath("messages[].message.title").description("유리병 메시지 제목"),
					fieldWithPath("messages[].message.postCardUrl").description("유리병 메시지 편지지"),
					fieldWithPath("messages[].message.negativity").description("유리병 메시지 위험도").optional()
				)
			));
	}

	@Test
	@DisplayName("API - 유리병 메시지 반응 삭제")
	void deleteBottleMessageReaction() throws Exception {
		//given
		BottleMessageReaction reaction = BottleMessageReaction.builder()
			.reactionType(ReactionType.LIKE)
			.message(bottleMessage1)
			.reactor(testUser)
			.build();

		CreateBottleMessageReactionRequest request = new CreateBottleMessageReactionRequest("LIKE");

		//when & then
		mockMvc.perform(delete("/api/bottle-messages/{messageId}/react", bottleMessage1.getId())
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-bottle-message-test/delete-bottle-messages-reaction",
				pathParameters(
					parameterWithName("messageId").description("반응을 삭제할 메시지 아이디")
				),
				requestFields(
					fieldWithPath("reaction").description("삭제할 반응(LIKE,EMPATHY,CHEER)")
				)
			));
	}

	@Test
	@DisplayName("API - 모든 편지지 목록 조회")
	void getAllPostcard() throws Exception {
		//when & then
		mockMvc.perform(get("/api/postcard/all")
						.header("Authorization", mockToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("api-bottle-message-test/get-all-postcard",
						responseFields(
								fieldWithPath("postcards[]").description("편지지 목록"),
								fieldWithPath("postcards[].id").description("편지지 아이디"),
								fieldWithPath("postcards[].url").description("편지지 url")
						)
				));
	}

	@Test
	@DisplayName("API - 보낸 편지지 숨김 처리")
	void hideSentMessage() throws Exception {
		//when & then
		mockMvc.perform(patch("/api/bottle-messages/{messageId}/hide", bottleMessage1.getId())
						.header("Authorization", mockToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("api-bottle-message-test/hide-sent-message",
						pathParameters(
								parameterWithName("messageId").description("숨김할 메시지 아이디")
						)
				));
	}

	@Test
	@DisplayName("API - 메시지 발신 가능여부 조회")
	void getIsAvailableSender() throws Exception {
		//when & then
		mockMvc.perform(get("/api/bottle-messages/available", testSender.getId())
						.header("Authorization", mockToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("api-bottle-message-test/is-available-sender",
						responseFields(
								fieldWithPath("isAvailableSender").description("발신가능여부")
						)
				));
	}
}
