package com.example.bloombackend.restdocs;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

import com.example.bloombackend.credit.controller.dto.request.PurchaseRequest;
import com.example.bloombackend.credit.entity.CreditType;
import com.example.bloombackend.credit.entity.UserCreditEntity;
import com.example.bloombackend.credit.repository.UserCreditRepository;
import com.example.bloombackend.global.config.JwtTokenProvider;
import com.example.bloombackend.item.entity.items.SeedEntity;
import com.example.bloombackend.item.repository.SeedRepository;
import com.example.bloombackend.oauth.OAuthProvider;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class CreditRestDocsTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SeedRepository seedRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserCreditRepository userCreditRepository;

	@SpyBean
	private JwtTokenProvider jwtTokenProvider;

	private UserEntity testUser;

	private String mockToken;

	private ObjectMapper objectMapper;

	private SeedEntity limitFlower;

	private UserCreditEntity button;

	private UserCreditEntity cash;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockToken = "jwtToken";
		testUser = userRepository.save(new UserEntity(OAuthProvider.KAKAO, "testUser", "testId"));
		doReturn(testUser.getId()).when(jwtTokenProvider).getUserIdFromToken(mockToken);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String limitDate = "2025-01-01";

		button = userCreditRepository.save(
			UserCreditEntity.builder()
				.creditType(CreditType.BUTTON)
				.balance(500)
				.user(testUser)
				.build()
		);

		cash = userCreditRepository.save(
			UserCreditEntity.builder()
				.creditType(CreditType.CASH)
				.balance(0)
				.user(testUser)
				.build()
		);

		limitFlower = seedRepository.save(
			SeedEntity.builder()
				.name("시즌한정 씨앗")
				.endDate(LocalDate.parse(limitDate, formatter))
				.isDefault(false)
				.thumbnailImgUrl("썸네일이미지")
				.smallIconUrl("./resources/static/flower-icons/freesia-small.svg")
				.bigIconUrl("./resources/static/flower-icons/freesia.svg")
				.price(300).build()
		);

	}

	@Test
	@DisplayName("API - 아이템 구매")
	void purchaseItemTest() throws Exception {
		// given
		PurchaseRequest request = new PurchaseRequest(limitFlower.getId(), CreditType.BUTTON);

		// when & then
		mockMvc.perform(post("/api/credit/purchase")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-credit-test/purchase",
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("itemId").description("구매할 아이템 ID"),
					fieldWithPath("creditType").description("사용할 크레딧 타입 (BUTTON, CASH)")),
				responseFields(
					fieldWithPath("purchasedItem").description("구매한 아이템 정보"),
					fieldWithPath("purchasedItem.id").description("아이템 ID"),
					fieldWithPath("purchasedItem.name").description("아이템 이름"),
					fieldWithPath("purchasedItem.price").description("아이템 가격"),
					fieldWithPath("purchasedItem.thumbnailUrl").description("아이템 썸네일 이미지 URL"),
					fieldWithPath("purchasedItem.type").description("아이템 타입"),
					fieldWithPath("purchasedItem.endDate").description("판매 종료일"),
					fieldWithPath("balance").description("남은 크레딧 잔액"),
					fieldWithPath("balance.buttonBalance").description("남은 버튼 잔액"),
					fieldWithPath("balance.cashBalance").description("남은 현금 잔액"))
			));
	}
}
