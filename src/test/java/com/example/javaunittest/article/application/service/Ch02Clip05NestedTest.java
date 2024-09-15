package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.application.port.out.CommandArticlePort;
import com.example.javaunittest.article.application.port.out.LoadArticlePort;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.ArticleFixtures;
import com.example.javaunittest.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Ch02Clip05NestedTest {
    private ArticleService sut;
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
    @DisplayName("articleId 로 조회시 Article 반환")
    void return_Article() {
        // given
        var article = ArticleFixtures.article();
        given(loadArticlePort.findArticleById(Mockito.anyLong()))
                .willReturn(Optional.of(article));

        // when
        var result = sut.getArticleById(1L);

        // then
        then(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", article.getId())
                .hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
                .hasFieldOrPropertyWithValue("subject", article.getSubject())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("username", article.getUsername())
                .hasFieldOrProperty("createdAt");
    }

    @Test
    @DisplayName("Article 존재하지 않을 경우 ResourceNotFoundException throw")
    void throw_ResourceNotFoundException() {
        // given
        given(loadArticlePort.findArticleById(any()))
                .willReturn(Optional.empty());

        // when & then
        thenThrownBy(() -> sut.getArticleById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Nested
    @DisplayName("Article 조회")
    class GetArticle {
        @Test
        @DisplayName("articleId 로 조회시 Article 반환")
        void return_Article() {
            // given
            var article = ArticleFixtures.article();
            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.of(article));

            // when
            var result = sut.getArticleById(1L);

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
        @DisplayName("존재하지 않을 경우 (ResourceNotFoundException throw")
        void throw_ResourceNotFoundException() {
            // given
            given(loadArticlePort.findArticleById(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getArticleById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("id : 1 게시물이 없습니다.");
        }
    }
}
