package com.example.javaunittest.article.adapter.in.api.dto;

import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;

public class BoardDto {



    public record BoardResponse(
            Long id,
            String name,
            BoardType boardType
    ) {
        public static BoardDto.BoardResponse of(Board board) {
            return new BoardDto.BoardResponse(
                    board.getId(),
                    board.getName(),
                    board.getBoardType()
            );
        }
    }
}