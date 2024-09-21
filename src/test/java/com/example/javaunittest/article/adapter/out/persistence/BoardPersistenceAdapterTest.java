package com.example.javaunittest.article.adapter.out.persistence;

import com.example.javaunittest.article.adapter.out.persistence.entity.BoardJpaEntity;
import com.example.javaunittest.article.adapter.out.persistence.repository.BoardRepository;
import com.example.javaunittest.article.domain.Board;
import com.example.javaunittest.article.domain.BoardType;
import com.example.javaunittest.out.persistence.BoardJpaEntityFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class BoardPersistenceAdapterTest {
    private BoardPersistenceAdapter adapter;
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        boardRepository = Mockito.mock(BoardRepository.class);
        adapter = new BoardPersistenceAdapter(boardRepository);
    }

    @Test
    void findBoardById() {
        // given
        BoardJpaEntity boardJpaEntity = BoardJpaEntityFixtures.board();
        given(boardRepository.findById(any()))
                .willReturn(Optional.of(boardJpaEntity));

        // when
        Optional<Board> result = adapter.findBoardById(5L);

        // then
        then(result)
                .isPresent()
                .hasValueSatisfying(board ->
                        then(board)
                                .isInstanceOf(Board.class)
                                .hasFieldOrPropertyWithValue("id", 5L)
                );
    }

    @Test
    void findAllByBoardType() {
        // given
        var boardType = BoardType.GENERAL;
        List<BoardJpaEntity> boardJpaEntityList = LongStream.range(1L, 4L)
                .mapToObj(it -> BoardJpaEntityFixtures.board(it, boardType))
                .toList();
        given(boardRepository.findAllByBoardType(boardType))
                .willReturn(boardJpaEntityList);

        // when
        List<Board> result = adapter.findAllByBoardType(boardType);

        // then
        then(result)
                .hasSize(3)
                .hasOnlyElementsOfType(Board.class);
    }
}
