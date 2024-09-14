package com.example.javaunittest.article.application.port.out;

import com.example.javaunittest.article.domain.Article;

public interface CommandArticlePort {
    Article createArticle(Article article);

    Article modifyArticle(Article article);

    void deleteArticle(Long articleId);
}
