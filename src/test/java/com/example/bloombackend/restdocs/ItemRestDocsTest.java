package com.example.bloombackend.restdocs;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.example.bloombackend.global.config.JwtTokenProvider;
import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.entity.ItemType;
import com.example.bloombackend.item.repository.ItemRepository;
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
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@SpyBean
	private JwtTokenProvider jwtTokenProvider;

	private UserEntity testUser;

	private String mockToken;

	private ObjectMapper objectMapper;

	private ItemEntity item1;
	private ItemEntity item2;
	private ItemEntity limitItem;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockToken = "jwtToken";
		testUser = userRepository.save(new UserEntity(OAuthProvider.KAKAO, "testUser", "testId"));
		doReturn(testUser.getId()).when(jwtTokenProvider).getUserIdFromToken(mockToken);

		item1 = itemRepository.save(
			ItemEntity.builder()
				.type(ItemType.SEED)
				.name("장미 씨앗")
				.imgUrl("http://...")
				.price(300)
				.isSale(true)
				.build()
		);

		item2 = itemRepository.save(
			ItemEntity.builder()
				.type(ItemType.SEED)
				.name("프리지아 씨앗")
				.imgUrl("http://...")
				.price(350)
				.isSale(true)
				.build()
		);

		limitItem = itemRepository.save(
			ItemEntity.builder()
				.type(ItemType.SEASON)
				.name("프리지아 씨앗")
				.imgUrl("http://...")
				.price(550)
				.isSale(false)
				.build()
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
				responseFields(
					fieldWithPath("items[]").description("판매 중인 아이템 목록"),
					fieldWithPath("items[].id").description("아이템 ID"),
					fieldWithPath("items[].name").description("아이템 이름"),
					fieldWithPath("items[].price").description("아이템 가격"),
					fieldWithPath("items[].imgUrl").description("아이템 이미지 URL"),
					fieldWithPath("items[].type").description("아이템 유형")
				)
			));
	}

}
