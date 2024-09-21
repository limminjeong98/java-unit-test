package com.example.javaunittest.article.domain;

public class BoardFixtures {
    public static Board board() {
        return new Board(5L, "board");
    }

    public static Board board(Long boardId) {
        return new Board(boardId, "board");
    }

    public static Board board(Long boardId, BoardType boardType) {
        return new Board(boardId, "board", boardType);
    }
}