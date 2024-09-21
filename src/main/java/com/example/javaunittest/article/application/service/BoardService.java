package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.application.port.in.GetBoardUseCase;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;
import com.example.javaunittest.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BoardService implements GetBoardUseCase {

    private final LoadBoardPort loadBoardPort;

    public BoardService(LoadBoardPort loadBoardPort) {
        this.loadBoardPort = loadBoardPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Board getBoard(Long boardId) {
        return loadBoardPort.findBoardById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> getBoardsByBoardType(BoardType boardType) {
        return loadBoardPort.findAllByBoardType(boardType);
    }
}
