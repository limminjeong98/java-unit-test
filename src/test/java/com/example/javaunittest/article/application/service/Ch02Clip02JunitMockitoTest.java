package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.application.port.out.CommandArticlePort;
import com.example.javaunittest.article.application.port.out.LoadArticlePort;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.Article;
import com.example.javaunittest.article.domain.ArticleFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class Ch02Clip02JunitMockitoTest {

    private ArticleService sut;

    private LoadArticlePort loadArticlePort;
    private CommandArticlePort commandArticlePort;
    private LoadBoardPort loadBoardPort;

    @BeforeEach
    void setUp() {
        loadArticlePort = Mockito.mock(LoadArticlePort.class);
        commandArticlePort = Mockito.mock(CommandArticlePort.class);
        loadBoardPort = Mockito.mock(LoadBoardPort.class);

        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

    @Test
    @DisplayName("articleId로 조회시 Article 반환")
    void return_Article() {
        // given
        Article article = ArticleFixtures.article();
        Mockito.when(loadArticlePort.findArticleById(any()))
                .thenReturn(Optional.of(article));

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
    @DisplayName("BDDStyle Board의 Article 목록 조회")
    void getArticlesByBoard_listArticles() {
        // given
        Article article1 = ArticleFixtures.article(1L);
        Article article2 = ArticleFixtures.article(2L);
        BDDMockito.given(loadArticlePort.findArticlesByBoardId(any()))
                .willReturn(List.of(article1, article2));

        // when
        List<Article> result = sut.getArticlesByBoardId(5L);

        // then
        then(result)
                .hasSize(2)
                .extracting("board.id").containsOnly(5L);
    }

    @Test
    @DisplayName("Article 삭제")
    void deleteArticle() {
        // given
        BDDMockito.willDoNothing()
                .given(commandArticlePort).deleteArticle(any());

        // when
        sut.deleteArticle(1L);

        // then
        verify(commandArticlePort).deleteArticle(1L);
    }

}
