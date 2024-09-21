package com.example.javaunittest.article.adapter.in.api;

import com.example.javaunittest.article.application.port.in.GetBoardUseCase;
import com.example.javaunittest.article.domain.BoardFixtures;
import com.example.javaunittest.article.domain.BoardType;
import com.example.javaunittest.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.LongStream;

import static com.example.javaunittest.article.domain.BoardType.GENERAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetBoardUseCase getBoardUseCase;

    @Nested
    @DisplayName("GET /board/{boardId}")
    class GetBoard {

        @Test
        @DisplayName("boardId에 해당하는 Board 정보 반환")
        void testGetBoardThen200OK() throws Exception {
            var boardId = 1L;
            var board = BoardFixtures.board(boardId);
            given(getBoardUseCase.getBoard(any())).willReturn(board);

            mockMvc
                    .perform(
                            get("/boards/{boardId}", boardId)
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.id").value(boardId),
                            jsonPath("$.name").value(board.getName())
                    );
        }

        @Test
        @DisplayName("boardId에 해당하는 Board가 없으면 400 Not Found 에러")
        void testGetBoardThen400NotFound() throws Exception {
            var boardId = 1L;
            given(getBoardUseCase.getBoard(any())).willThrow(ResourceNotFoundException.class);

            mockMvc
                    .perform(
                            get("/boards/{boardId}", boardId)
                    )
                    .andExpectAll(
                            status().isNotFound()
                    );
        }
    }

    @Nested
    @DisplayName("GET /boards?boardType={boardType}")
    class GetBoardsByBoardType {
        @Test
        void testGetBoardsByBoardType() throws Exception {
            BoardType boardType = GENERAL;
            var boardList = LongStream.range(1L, 4L)
                    .mapToObj(BoardFixtures::board)
                    .toList();
            given(getBoardUseCase.getBoardsByBoardType(boardType)).willReturn(boardList);

            mockMvc.perform(
                    get("/boards?boardType={boardType}", boardType)
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$.size()").value(3),
                    jsonPath("$.[0].boardType").value(GENERAL.toString())
            );
        }
    }
}