package com.example.bloombackend.restdocs;

import com.example.bloombackend.quote.entity.QuoteEntity;
import com.example.bloombackend.quote.repository.QuoteRepository;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class QuoteRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private QuoteRepository quoteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("API - 오늘의 명언 조회")
    void getTodayQuoteTest() throws Exception {
        QuoteEntity quoteEntity = new QuoteEntity("오늘도 멋진 하루 되세요!", "Bloom");
        quoteRepository.save(quoteEntity);

        mockMvc.perform(get("/api/quote")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("quote/get-today-quote",
                        responseFields(
                                fieldWithPath("quote").description("명언 내용"),
                                fieldWithPath("author").description("명언 작가")
                        )
                ));
    }

    @Test
    @DisplayName("API - 명언 데이터가 존재하지 않는 경우")
    void getTodayQuote_NoDataTest() throws Exception {
        mockMvc.perform(get("/api/quote")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("quote/get-today-quote-no-data",
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("API - 오늘 날짜의 아이디의 명언이 존재하지 않는 경우")
    void getTodayQuote_NotFoundTest() throws Exception {
        doReturn(Optional.empty()).when(quoteRepository).findById(anyLong());

        mockMvc.perform(get("/api/quote")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("quote/get-today-quote-not-found",
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }
}