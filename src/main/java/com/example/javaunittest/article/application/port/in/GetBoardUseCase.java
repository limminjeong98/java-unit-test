package com.example.javaunittest.article.application.port.in;

import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;

import java.util.List;

public interface GetBoardUseCase {
    Board getBoard(Long boardId);

    List<Board> getBoardsByBoardType(BoardType boardType);
}
