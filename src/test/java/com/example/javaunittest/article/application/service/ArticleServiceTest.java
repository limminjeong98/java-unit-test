package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.adapter.in.api.dto.ArticleDto;
import com.example.javaunittest.article.adapter.in.api.dto.BoardDto;
import com.example.javaunittest.article.application.port.out.CommandArticlePort;
import com.example.javaunittest.article.application.port.out.LoadArticlePort;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.Article;
import com.example.javaunittest.article.domain.ArticleFixtures;
import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardFixtures;
import com.example.javaunittest.common.exception.AccessDeniedException;
import com.example.javaunittest.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    // System Under Test (테스트 대상 시스템)
    private ArticleService sut;

    // 목 객체 생성하는 2가지 방법 있음
    // 1. @Mock 어노테이션으로 모킹할 경우 @ExtendWith(MockitoExtension.class) 필요
    // 2. Mockito.mock()으로 할 경우 @ExtendWith(MockitoExtension.class)  없어도 괜찮음
    @Mock
    private LoadArticlePort loadArticlePort;

    @Mock
    private CommandArticlePort commandArticlePort;

    @Mock
    private LoadBoardPort loadBoardPort;

    @BeforeEach
    void setUp() {
        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

    @Test
    @DisplayName("Board의 Article 목록 조회")
    void getArticlesByBoard_listArticles() {
        // given
        Article article1 = ArticleFixtures.article(1L);
        Article article2 = ArticleFixtures.article(2L);
        given(loadArticlePort.findArticlesByBoardId(any()))
                .willReturn(List.of(article1, article2));

        // when
        List<Article> result = sut.getArticlesByBoardId(5L);

        // then
        then(result)
                .hasSize(2)
                .extracting("board.id").containsOnly(5L);

    }

    @Nested
    @DisplayName("Article 한 개 조회")
    class GetArticle {
        @Test
        @DisplayName("articleId로 조회시 Article 반화")
        void return_Article() {
            // given
            Article article = ArticleFixtures.article();
            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.of(article));

            // when
            Article result = sut.getArticleById(1L);

            // then
            then(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", article.getId())
                    .hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
                    .hasFieldOrPropertyWithValue("subject", article.getSubject())
                    .hasFieldOrPropertyWithValue("content", article.getContent())
                    .hasFieldOrPropertyWithValue("username", article.getUsername())
                    .hasFieldOrPropertyWithValue("createdAt", article.getCreatedAt());
        }

        @Test
        @DisplayName("존재하지 않을 경우 ResourceNotFoundException throw")
        void throw_NoSuchElementException() {
            // given
            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getArticleById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("id : 1 게시물이 없습니다.");
        }
    }

    @Nested
    @DisplayName("Article 한 개 등록")
    class PostArticle {
        static Stream<Arguments> invalidParameters() {
            return Stream.of(
                    Arguments.of("subject null", null, "content", "user"),
                    Arguments.of("subject empty", "", "content", "user"),
                    Arguments.of("content null", "subject", null, "user"),
                    Arguments.of("content empty", "subject", "", "user"),
                    Arguments.of("username null", "subject", "content", null)
            );
        }

        @Test
        @DisplayName("생성된 Article 반환")
        void returnCreatedArticleId() {
            // given
            ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(5L, "subject", "content", "user");
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

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidParameters")
        @DisplayName("정상적이지 않은 param이면 IllegalArgumentException")
        void throwIllegalArgumentException(String name, String subject, String content, String username) {
            // given
            ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(5L, subject, content, username);

            // when & then
            thenThrownBy(() -> sut.createArticle(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Article 변경")
    class ModifyArticle {
        private ArticleDto.UpdateArticleRequest request;

        @BeforeEach
        void setUp() {
            request = new ArticleDto.UpdateArticleRequest(6L, new BoardDto(6L, "board"), "new subject", "new content", "user");
        }

        @Test
        @DisplayName("변경된 Article 반환")
        void returnModifiedArticleId() {
            // given
            Article article = ArticleFixtures.article();
            Board board = new Board(6L, "other board");

            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.of(article));
            Article modifiedArticle = Article.builder()
                    .id(article.getId())
                    .board(board)
                    .subject("new subject")
                    .content("new content")
                    .username(article.getUsername())
                    .createdAt(article.getCreatedAt())
                    .build();
            given(commandArticlePort.modifyArticle(any()))
                    .willReturn(modifiedArticle);

            // when
            Article result = sut.modifyArticle(request);

            // then
            then(result)
                    .isEqualTo(modifiedArticle);
        }

        @Test
        @DisplayName("존재하지 않는 Article이면 NoSuchElementException")
        void notExistArticle_throwNoSuchElementException() {
            // given
            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.modifyArticle(request))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("user가 다르면 AccessDeniedException throw")
        void otherUser_throwException() {
            // given
            ArticleDto.UpdateArticleRequest request = new ArticleDto.UpdateArticleRequest(6L, new BoardDto(6L, "board"), "new subject", "new content", "other user");
            Article article = ArticleFixtures.article();
            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.of(article));

            // when & then
            thenThrownBy(() -> sut.modifyArticle(request))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @Nested
    class DeleteArticle {
        @Test
        @DisplayName("Article 삭제")
        void deleteArticle() {
            // given
            willDoNothing()
                    .given(commandArticlePort).deleteArticle(any());

            // when
            sut.deleteArticle(1L);

            // then
            verify(commandArticlePort).deleteArticle(1L);
        }
    }

}
