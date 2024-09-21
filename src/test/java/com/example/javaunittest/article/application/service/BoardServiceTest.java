package com.example.javaunittest.article.application.service;

import com.example.javaunittest.article.application.port.out.LoadBoardPort;
import com.example.javaunittest.article.domain.BoardFixtures;
import com.example.javaunittest.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.LongStream;

import static com.example.javaunittest.article.domain.BoardType.GENERAL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    private BoardService sut;

    @Mock
    private LoadBoardPort loadBoardPort;

    @BeforeEach
    void setUp() {
        sut = new BoardService(loadBoardPort);
    }

    @Nested
    @DisplayName("test getBoard(boardId)")
    class GetBoard {
        @Test
        @DisplayName("Board가 존재하면 Board 반환")
        void existBoardThenReturnBoard() {
            // given
            var boardId = 1L;
            var board = BoardFixtures.board(boardId);
            given(loadBoardPort.findBoardById(any()))
                    .willReturn(Optional.of(board));

            // when
            var result = sut.getBoard(boardId);

            // then
            then(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", boardId)
                    .hasFieldOrPropertyWithValue("name", board.getName());
        }

        @Test
        @DisplayName("Board가 존재하지 않으면 ResourceNotFoundException throw")
        void notExistBoardThenThrowResourceNotFoundException() {
            // given
            var boardId = 1L;
            given(loadBoardPort.findBoardById(any()))
                    .willReturn(Optional.empty());

            // when
            thenThrownBy(() -> sut.getBoard(boardId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("test getBoardsByBoardType(boardType)")
    class GetBoardsByBoardType {
        @Test
        @DisplayName("boardType에 해당하는 Board 목록 반환")
        void getBoards() {
            // given
            var boardType = GENERAL;
            var boardList = LongStream.range(1L, 4L)
                    .mapToObj(it -> BoardFixtures.board(it, GENERAL))
                    .toList();
            given(loadBoardPort.findAllByBoardType(any())).willReturn(
                    boardList
            );

            // when
            var result = sut.getBoardsByBoardType(boardType);

            // then
            then(result)
                    .isNotNull()
                    .hasSize(3)
                    .extracting("boardType").containsOnly(boardType);
        }
    }
}