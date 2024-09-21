package com.example.javaunittest.article.adapter.out.persistence.entity;

import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board")
@NoArgsConstructor
@Getter
public class BoardJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false)
    private BoardType boardType;

    public BoardJpaEntity(String name) {
        this.name = name;
        this.boardType = BoardType.GENERAL;
    }

    private BoardJpaEntity(Long id, String name) {
        this.id = id;
        this.name = name;
        this.boardType = BoardType.GENERAL;
    }

    public static BoardJpaEntity fromDomain(Board board) {
        return new BoardJpaEntity(board.getId(), board.getName());
    }

    public Board toDomain() {
        return new Board(this.id, this.name, this.boardType);
    }
}
