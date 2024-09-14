package com.example.javaunittest.article.application.port.in;

import com.example.javaunittest.article.domain.Article;

import java.util.List;

public interface GetArticleUseCase {
    Article getArticleById(Long articleId);

    List<Article> getArticlesByBoardId(Long boardId);
}
