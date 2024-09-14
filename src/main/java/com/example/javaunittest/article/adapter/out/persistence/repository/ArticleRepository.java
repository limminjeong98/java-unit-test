package com.example.javaunittest.article.adapter.out.persistence.repository;

import com.example.javaunittest.article.adapter.out.persistence.entity.ArticleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleJpaEntity, Long> {
    List<ArticleJpaEntity> findByBoardId(Long boardId);
}