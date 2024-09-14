package com.example.javaunittest.article.application.port.out;

import com.example.javaunittest.article.domain.Board;

import java.util.Optional;

public interface LoadBoardPort {
    Optional<Board> findBoardById(Long boardId);
}
