package com.example.javaunittest.article.adapter.out.persistence.repository;

import com.example.javaunittest.article.adapter.out.persistence.entity.ArticleJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@Sql("/data/ArticleRepositoryFixtureTest.sql")
public class Ch04Clip02ArticleRepositoryFixtureTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void listAllArticles() {
        // given & when
        List<ArticleJpaEntity> result = articleRepository.findByBoardId(5L);

        // then
        then(result)
                .hasSize(2);
    }

    @Test
    @Sql("/data/ArticleRepositoryFixtureTest.listAllArticles2.sql")
    void listAllArticles2() {
        // given & when
        List<ArticleJpaEntity> result = articleRepository.findByBoardId(5L);

        // then
        then(result)
                .hasSize(3);
    }
}
