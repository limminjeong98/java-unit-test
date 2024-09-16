package com.example.javaunittest.article.adapter.in.api;

import com.example.javaunittest.article.adapter.in.api.dto.ArticleDto;
import com.example.javaunittest.article.application.port.in.CreateArticleUseCase;
import com.example.javaunittest.article.application.port.in.DeleteArticleUseCase;
import com.example.javaunittest.article.application.port.in.GetArticleUseCase;
import com.example.javaunittest.article.application.port.in.ModifyArticleUseCase;
import com.example.javaunittest.article.domain.Article;
import com.example.javaunittest.article.domain.ArticleFixtures;
import com.example.javaunittest.common.api.GlobalControllerAdvice;
import com.example.javaunittest.common.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Ch03Clip01ArticleControllerUnitTest {

    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
            .serializers(LocalTimeSerializer.INSTANCE)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .modules(new JavaTimeModule())
            .build();
    private MockMvc mockMvc;
    private GetArticleUseCase getArticleUseCase;
    private CreateArticleUseCase createArticleUseCase;
    private ModifyArticleUseCase modifyArticleUseCase;
    private DeleteArticleUseCase deleteArticleUseCase;

    @BeforeEach
    void setUp() {
        getArticleUseCase = Mockito.mock(GetArticleUseCase.class);
        createArticleUseCase = Mockito.mock(CreateArticleUseCase.class);
        modifyArticleUseCase = Mockito.mock(ModifyArticleUseCase.class);
        deleteArticleUseCase = Mockito.mock(DeleteArticleUseCase.class);

        // @SpringBootTest 아니므로 직접 MockMvc 설정 필요
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ArticleController(getArticleUseCase, createArticleUseCase, modifyArticleUseCase, deleteArticleUseCase))
                .alwaysDo(print())
                .setControllerAdvice(new GlobalControllerAdvice()) // @ControllerAdvice 어노테이션으로 스프링이 빈 등록 및 의존성 주입해주던 것 직접 처리
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Nested
    @DisplayName("GET /articles/{articleId}")
    class GetArticle {
        @Test
        @DisplayName("Article이 있으면, 200 OK return response")
        void returnResponse() throws Exception {
            // given
            Article article = ArticleFixtures.article();
            given(getArticleUseCase.getArticleById(any()))
                    .willReturn(article);

            // when & then
            Long articleId = 1L;
            mockMvc
                    .perform(get("/articles/{articleId}", articleId))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("articleID에 해당하는 Article이 없으면 400 Not Found")
        void notFound() throws Exception {
            // given
            given(getArticleUseCase.getArticleById(any()))
                    .willThrow(new ResourceNotFoundException("article not exists"));

            // when & then
            Long articleId = 1L;
            mockMvc
                    .perform(get("/articles/{articleId}", articleId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /articles")
    class PostArticle {
        @Test
        @DisplayName("생성된 articleId 반환")
        void create_returnArticleId() throws Exception {
            // given
            Article createdArticle = ArticleFixtures.article();
            given(createArticleUseCase.createArticle(any()))
                    .willReturn(createdArticle);

            // when & then
            String body = objectMapper.writeValueAsString(Map.of("boardId", 5L, "subject", "subject", "content", "content", "username", "user"));
            mockMvc.perform(
                    post("/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            ).andExpect(
                    status().isOk()
            );
        }

        @ParameterizedTest(name = "{0}")
        @DisplayName("비정상 파라미터이면 BadRequest 반환")
        @CsvSource(
                value = {
                        "subject is null,,content,user",
                        "content is null,subject,,user",
                        "username is null,subject,content,",
                        "username is empty,subject,content,''"
                }
        )
        void invalidParam_BadRequest(String desc, String subject, String content, String username) throws Exception {
            // given
            String body = objectMapper.writeValueAsString(new ArticleDto.CreateArticleRequest(5L, subject, content, username));

            // when & then
            mockMvc.perform(
                            post("/articles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    ).andDo(print())
                    .andExpect(
                            status().isBadRequest()
                    );
        }
    }


}
