package com.example.javaunittest.article.application.port.out;

import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;

import java.util.List;
import java.util.Optional;

public interface LoadBoardPort {
    Optional<Board> findBoardById(Long boardId);

    List<Board> findAllByBoardType(BoardType boardType);
}
