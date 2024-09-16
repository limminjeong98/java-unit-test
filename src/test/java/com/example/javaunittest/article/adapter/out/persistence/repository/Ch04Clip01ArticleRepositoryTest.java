package com.example.javaunittest.article.adapter.out.persistence.repository;

import com.example.javaunittest.article.adapter.out.persistence.entity.ArticleJpaEntity;
import com.example.javaunittest.article.adapter.out.persistence.entity.BoardJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
public class Ch04Clip01ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager entityManager;

    private BoardJpaEntity boardJpaEntity;

    @BeforeEach
    void setUp() {
        boardJpaEntity = entityManager.persist(new BoardJpaEntity("test"));

        entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject1", "content1", "user", LocalDateTime.now()));
        entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject2", "content2", "user", LocalDateTime.now()));
    }

    @Test
    void listAllArticles() {
        // given & when
        List<ArticleJpaEntity> result = articleRepository.findByBoardId(boardJpaEntity.getId());

        // then
        then(result)
                .hasSize(2);
    }
}
