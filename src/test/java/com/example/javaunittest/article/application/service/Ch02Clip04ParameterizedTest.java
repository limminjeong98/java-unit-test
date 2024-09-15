package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.adapter.in.api.dto.ArticleDto;
import com.example.javaunittest.article.application.port.out.CommandArticlePort;
import com.example.javaunittest.article.application.port.out.LoadArticlePort;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.Article;
import com.example.javaunittest.article.domain.ArticleFixtures;
import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Ch02Clip04ParameterizedTest {

    private ArticleService sut;
    @Mock
    private LoadArticlePort loadArticlePort;
    @Mock
    private CommandArticlePort commandArticlePort;
    @Mock
    private LoadBoardPort loadBoardPort;

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("subject is null", null, "content", "user"),
                Arguments.of("subject is empty", "", "content", "user"),
                Arguments.of("content is null", "subject", null, "user"),
                Arguments.of("content is empty", "subject", "", "user"),
                Arguments.of("username is null", "subject", "content", null)
        );
    }

    @BeforeEach
    void setUp() {
        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

    @ParameterizedTest
    @ValueSource(strings = {"english", "한글", "!@#$"})
    @DisplayName("영어, 한글, 특수문자로 제목 생성")
    void valueSource_throwIllegalArgumentException(String subject) {
        // given
        ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(5L, subject, "content", "user");
        Board board = BoardFixtures.board();
        given(loadBoardPort.findBoardById(any()))
                .willReturn(Optional.of(board));
        Article createdArticle = ArticleFixtures.article();
        given(commandArticlePort.createArticle(any()))
                .willReturn(createdArticle);

        // when
        Article result = sut.createArticle(request);

        // then
        then(result)
                .isEqualTo(createdArticle);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isEmpty_throwIllegalArgumentException(String subject) {
        // given
        ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(5L, subject, "content", "user");

        // when & then
        thenThrownBy(() -> sut.createArticle(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource(
            value = {
                    "2,3,5",
                    "1,5,6",
                    "10,11,21"
            }
    )
    @DisplayName("2개의 합")
    void csvSource_addNumber(Integer a, Integer b, Integer sum) {
        then(sum)
                .isEqualTo(a + b);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidParameters")
    @DisplayName("정상적이지 않은 param 이면 IllegalArgumentException")
    void methodSource_throwIllegalArgumentException(String name, String subject, String content, String username) {
        // given
        ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(5L, subject, content, username);

        // when & then
        thenThrownBy(() -> sut.createArticle(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
