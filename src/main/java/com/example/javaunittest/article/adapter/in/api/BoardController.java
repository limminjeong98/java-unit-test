package com.example.javaunittest.article.adapter.in.api;

import com.example.javaunittest.article.adapter.in.api.dto.BoardDto;
import com.example.javaunittest.article.application.port.in.GetBoardUseCase;
import com.example.javaunittest.article.domain.BoardType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final GetBoardUseCase getBoardUseCase;

    public BoardController(GetBoardUseCase getBoardUseCase) {
        this.getBoardUseCase = getBoardUseCase;
    }

    @GetMapping("/{boardId}")
    BoardDto.BoardResponse getBoard(@PathVariable Long boardId) {
        return BoardDto.BoardResponse.of(getBoardUseCase.getBoard(boardId));
    }

    @GetMapping
    List<BoardDto.BoardResponse> getBoardsByType(@RequestParam BoardType boardType) {
        return getBoardUseCase.getBoardsByBoardType(boardType)
                .stream()
                .map(BoardDto.BoardResponse::of)
                .toList();
    }
}
