package com.K_oin.Koin.Controller;

import com.K_oin.Koin.DTO.ApiResponse;
import com.K_oin.Koin.DTO.boardDTOs.BoardDTO;
import com.K_oin.Koin.DTO.boardDTOs.BoardDetailDTO;
import com.K_oin.Koin.DTO.boardDTOs.BoardSummaryDTO;
import com.K_oin.Koin.Service.boardServices.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BoardSummaryDTO>>> getBoardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "FREEBOARD") String boardType
    ) {
        Page<BoardSummaryDTO> boardPage;
        List<BoardSummaryDTO> boardList;
        try {
            boardList = boardService.getBoardList(page, size, sortBy, boardType);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }

        ApiResponse<List<BoardSummaryDTO>> response = new ApiResponse<>(
                true,
                boardList,
                "게시글 목록 조회 성공"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createBoard") // 게시판 만들기
    public ResponseEntity<ApiResponse<BoardDTO>> createBoard(@RequestBody BoardDTO boardDTO, Authentication authentication) {

        try {
                boardService.createBoard(boardDTO, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "게시글 생성 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, boardDTO, "게시글 생성 성공"));
    }

    @PostMapping("/BoardLike/{boardId}")
    public ResponseEntity<ApiResponse<String>> likeBoard(@PathVariable Long boardId, Authentication authentication) {

        String message = "";

        try {
           message = boardService.likeBoard(boardId, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "게시판 번호: " + boardId, "게시판 좋아요 기능 오류: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "게시판 번호: " + boardId, message));
    }

    @GetMapping("/{boardType}/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailDTO>> getBoardType(@PathVariable String boardType, @PathVariable Long boardId) {

        BoardDetailDTO boardDetailDTO;

        try {
            boardDetailDTO = boardService.getBoardDetail(boardType, boardId);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "게시판 유형 조회 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, boardDetailDTO, "게시판 유형 조회 성공"));
    }

    @DeleteMapping("delete/{boardId}")
    public ResponseEntity<ApiResponse<String>> deleteBoard(@PathVariable Long boardId, Authentication authentication) {
        try {
            boardService.deleteBoard(boardId, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "게시판 번호: " + boardId, "게시판 삭제 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "게시판 번호: " + boardId, "게시판 삭제 성공"));
    }
}
