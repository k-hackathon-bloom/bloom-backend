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

import com.example.bloombackend.credit.entity.CreditType;
import com.example.bloombackend.credit.entity.UserCreditEntity;
import com.example.bloombackend.credit.repository.UserCreditRepository;
import com.example.bloombackend.global.config.JwtTokenProvider;
import com.example.bloombackend.item.entity.UserItemEntity;
import com.example.bloombackend.item.entity.items.SeedEntity;
import com.example.bloombackend.item.repository.SeedRepository;
import com.example.bloombackend.item.repository.UserItemRepository;
import com.example.bloombackend.oauth.OAuthProvider;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class ItemRestDocsTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SeedRepository seedRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserItemRepository userItemRepository;

	@Autowired
	private UserCreditRepository userCreditRepository;

	@SpyBean
	private JwtTokenProvider jwtTokenProvider;

	private UserEntity testUser;

	private String mockToken;

	private ObjectMapper objectMapper;

	private SeedEntity rose;

	private SeedEntity limitFlower;

	private SeedEntity defaultFlower;

	private UserItemEntity userItem;

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
				.balance(0)
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

		rose = seedRepository.save(
			SeedEntity.builder()
				.name("장미 씨앗")
				.endDate(LocalDate.of(9999, 12, 31))
				.isDefault(false)
				.thumbnailImgUrl("썸네일이미지")
				.smallIconUrl("./resources/static/flower-icons/rose-small.svg")
				.bigIconUrl("./resources/static/flower-icons/rose.svg")
				.price(300).build()
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

		defaultFlower = seedRepository.save(
			SeedEntity.builder()
				.name("기본씨앗 씨앗")
				.endDate(LocalDate.of(9999, 12, 31))
				.isDefault(true)
				.thumbnailImgUrl("썸네일이미지")
				.smallIconUrl("./resources/static/flower-icons/freesia-small.svg")
				.bigIconUrl("./resources/static/flower-icons/freesia.svg")
				.price(300).build()
		);

		userItem = userItemRepository.save(
			UserItemEntity.builder()
				.userId(testUser.getId())
				.item(limitFlower).build()
		);

	}

	@Test
	@DisplayName("API - 판매중인 아이템 목록 조회")
	void getItemsTest() throws Exception {
		// when & then
		mockMvc.perform(get("/api/item/sale")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-item-test/get-items",
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("items[]").description("판매 중인 아이템 목록"),
					fieldWithPath("items[].id").description("아이템 ID"),
					fieldWithPath("items[].name").description("아이템 이름"),
					fieldWithPath("items[].price").description("아이템 가격"),
					fieldWithPath("items[].thumbnailUrl").description("아이템 구매용 이미지 URL"),
					fieldWithPath("items[].type").description("아이템 유형"),
					fieldWithPath("items[].endDate").description(
						"판매 마감일 +" + "\n" +
							"- 시즌 한정 아이템의 경우, 실제 마감일이 표시됩니다. +" + "\n" +
							"- 지속 판매 아이템의 경우, \"9999-12-31\"로 고정됩니다.")
				)
			));
	}

	@Test
	@DisplayName("API - 유저의 인벤토리 아이템 목록 조회")
	void getUserItemsTest() throws Exception {
		// when & then
		// 테스트 실행
		mockMvc.perform(get("/api/item/inventory")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-item-test/get-user-items",
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("defaultItems[]").description("유저가 가진 기본 아이템 목록"),
					fieldWithPath("defaultItems[].id").description("아이템 ID"),
					fieldWithPath("defaultItems[].name").description("아이템 이름"),
					fieldWithPath("defaultItems[].price").description("아이템 가격"),
					fieldWithPath("defaultItems[].thumbnailUrl").description("아이템 썸네일 이미지 URL"),
					fieldWithPath("defaultItems[].type").description("아이템 타입"),
					fieldWithPath("defaultItems[].endDate").description("판매 종료일"),

					fieldWithPath("purchasedItems[]").description("유저가 구매한 아이템 목록"),
					fieldWithPath("purchasedItems[].id").description("아이템 ID"),
					fieldWithPath("purchasedItems[].name").description("아이템 이름"),
					fieldWithPath("purchasedItems[].price").description("아이템 가격"),
					fieldWithPath("purchasedItems[].thumbnailUrl").description("아이템 썸네일 이미지 URL"),
					fieldWithPath("purchasedItems[].type").description("아이템 타입"),
					fieldWithPath("purchasedItems[].endDate").description("판매 종료일")
				)
			));
	}
}
