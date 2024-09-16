package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.adapter.in.api.dto.ArticleDto;
import com.example.javaunittest.article.application.port.out.CommandArticlePort;
import com.example.javaunittest.article.application.port.out.LoadArticlePort;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@ExtendWith(MockitoExtension.class)
public class Ch02Clip03ExceptionTest {
    private final Board board = BoardFixtures.board();
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
    @DisplayName("subject가 정상적이지 않으면 IllegalArgumentException")
    void throwIllegalArgumentException() {
        // given
        ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(5L, null, "content", "user");

        // when & then
        thenThrownBy(() -> sut.createArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("subject should");
    }
}
