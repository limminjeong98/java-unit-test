package com.example.javaunittest.article.application.port.in;

import com.example.javaunittest.article.adapter.in.api.dto.ArticleDto;
import com.example.javaunittest.article.domain.Article;

public interface ModifyArticleUseCase {
    Article modifyArticle(ArticleDto.UpdateArticleRequest request);
}
