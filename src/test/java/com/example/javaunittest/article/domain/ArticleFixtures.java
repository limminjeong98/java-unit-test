package com.example.javaunittest.article.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFixtures {
    public static Article article(Long id) {
        Board board = new Board(5L, "board");
        return new Article(id, board, "subject" + id, "content" + id, "user" + id,
                LocalDateTime.parse("2024-09-15T10:20:30").plusDays(id));
    }

    public static Article article() {
        Board board = new Board(5L, "board");
        return new Article(1L, board, "subject", "content", "user",
                LocalDateTime.parse("2024-02-01T10:20:30"));
    }
}
