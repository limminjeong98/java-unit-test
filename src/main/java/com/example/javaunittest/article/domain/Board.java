package com.example.javaunittest.article.domain;

import lombok.Getter;

@Getter
public class Board {
    private final Long id;
    private final String name;

    public Board(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
