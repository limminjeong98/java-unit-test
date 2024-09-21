package com.example.javaunittest.article.adapter.out.persistence;

import com.example.javaunittest.article.adapter.out.persistence.entity.BoardJpaEntity;
import com.example.javaunittest.article.adapter.out.persistence.repository.BoardRepository;
import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BoardPersistenceAdapter implements LoadBoardPort {
    private final BoardRepository boardRepository;

    public BoardPersistenceAdapter(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    @Override
    public Optional<Board> findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardJpaEntity::toDomain);
    }

    @Override
    public List<Board> findAllByBoardType(BoardType boardType) {
        return boardRepository.findAllByBoardType(boardType)
                .stream()
                .map(BoardJpaEntity::toDomain)
                .toList();
    }
}
