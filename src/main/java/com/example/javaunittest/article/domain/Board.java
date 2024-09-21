package com.example.javaunittest.article.domain;

import lombok.Getter;

@Getter
public class Board {
    private final Long id;
    private final String name;
    private final BoardType boardType;

    public Board(Long id, String name, BoardType boardType) {
        this.id = id;
        this.name = name;
        this.boardType = boardType;
    }

    public Board(Long id, String name) {
        this.id = id;
        this.name = name;
        this.boardType = BoardType.GENERAL;
    }
}
