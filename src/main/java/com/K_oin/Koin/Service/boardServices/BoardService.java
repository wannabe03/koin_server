package com.K_oin.Koin.Service.boardServices;

import com.K_oin.Koin.DTO.boardDTOs.BoardDTO;
import com.K_oin.Koin.DTO.boardDTOs.BoardDetailDTO;
import com.K_oin.Koin.DTO.boardDTOs.BoardSummaryDTO;
import com.K_oin.Koin.DTO.commentDTOs.CommentDetailDTO;
import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import com.K_oin.Koin.Entitiy.BoardEntity.Board;
import com.K_oin.Koin.Entitiy.BoardEntity.Likes.BoardLike;
import com.K_oin.Koin.EnumData.BoardType;
import com.K_oin.Koin.Repository.boardRepository.Likes.BoardLikeRepository;
import com.K_oin.Koin.Repository.boardRepository.BoardRepository;
import com.K_oin.Koin.Repository.userRepository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    public List<BoardSummaryDTO> getBoardList(int page, int size, String sortBy, String boardType) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Page<Board> boards;

            BoardType type = BoardType.valueOf(boardType.toUpperCase());

            switch (sortBy.toLowerCase()) {
                case "likes":
                    boards = boardRepository.findAllByBoardTypeOrderByLikesDesc(type, pageable);
                    break;
                case "latest":
                default:
                    boards = boardRepository.findAllByBoardTypeOrderByCreatedAtDesc(type, pageable);
            }

            return boards.getContent().stream()
                    .map(board -> {
                        BoardAuthorDTO authorDTO = null;

                        // 익명 게시글이 아닐 경우 작성자 정보 세팅
                        if (!board.isAnonymous()) {
                            authorDTO = BoardAuthorDTO.builder()
                                    .nickname(board.getAuthor().getNickname())
                                    .nationality(board.getAuthor().getNationality().name())
                                    .build();
                        }

                        return BoardSummaryDTO.builder()
                            .boardId(board.getBoardId())
                            .title(board.getTitle())
                            .boardAuthorDTO(authorDTO)
                            .preview(board.getBody().length() > 100
                                    ? board.getBody().substring(0, 100) + "..."
                                    : board.getBody())
                            .likeCount(board.getLikes().size())
                            .commentCount(board.getComments().size())
                            .createdAt(board.getCreatedAt())
                            .build();
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("게시글 목록 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public void createBoard(BoardDTO boardDTO, String userName) {
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        try {
            Board board = Board.builder()
                    .title(boardDTO.getTitle())
                    .body(boardDTO.getBody())
                    .boardType(BoardType.valueOf(boardDTO.getBoardType().toUpperCase()))
                    .author(user)
                    .createdAt(LocalDateTime.now())
                    .anonymous(boardDTO.isAnonymous()) // 기본값 설정, 필요에 따라 DTO에 추가 가능
                    .build();

            boardRepository.save(board);
            log.info("게시글 생성 성공 - boardId: {}, title: {}", board.getBoardId(), board.getTitle());
        } catch (Exception e) {
            log.error("게시글 생성 실패 - 사용자: {}, 오류: {}", userName, e.getMessage());
            throw new RuntimeException("게시글 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public BoardDetailDTO getBoardDetail(String boardType, Long boardId) {

        BoardType type = BoardType.valueOf(boardType.toUpperCase());

        BoardAuthorDTO boardAuthorDTO = null;

        Board board = boardRepository.findByBoardTypeAndBoardId(type, boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!board.isAnonymous()) {
            boardAuthorDTO = BoardAuthorDTO.builder()
                    .nickname(board.getAuthor().getNickname())
                    .nationality(board.getAuthor().getNationality().name())
                    .build();
        }

        return BoardDetailDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .body(board.getBody())
                .boardAuthorDTO(boardAuthorDTO)
                .createdAt(board.getCreatedAt())
                .likeCount(board.getLikes().size())
                .anonymous(board.isAnonymous())
                .commentCount(board.getComments().size())
                .comments(board.getComments().stream()
                        .map(comment -> {
                            BoardAuthorDTO commentAuthorDTO = null;

                            // 익명 댓글이 아닐 경우만 작성자 정보 세팅
                            if (!comment.isAnonymous()) {
                                commentAuthorDTO = BoardAuthorDTO.builder()
                                        .nickname(comment.getAuthor().getNickname())
                                        .nationality(comment.getAuthor().getNationality().name())
                                        .build();
                            }

                            return CommentDetailDTO.builder()
                                    .commentId(comment.getCommentId())
                                    .author(commentAuthorDTO)
                                    .body(comment.getContent())
                                    .likeCount(comment.getLikes().size())
                                    .anonymous(comment.isAnonymous())
                                    .createdDate(comment.getCreatedAt())
                                    .build();
                        })
                        .toList())
                .build();
    }

    public void deleteBoard(Long boardId, String name) {
        var user = userRepository.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + name));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!board.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }

        try {
            boardRepository.delete(board);
            log.info("게시글 삭제 성공 - boardId: {}, title: {}", board.getBoardId(), board.getTitle());
        } catch (Exception e) {
            log.error("게시글 삭제 실패 - 사용자: {}, 오류: {}", name, e.getMessage());
            throw new RuntimeException("게시글 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public String likeBoard(Long boardId, String userName) {
        String message = "";

        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board);

        if (boardLike != null) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            boardLikeRepository.delete(boardLike);
            message = "게시판 좋아요 취소 성공";
            log.info("게시글 좋아요 취소 - boardId: {}, user: {}", boardId, userName);
        } else {
            // 좋아요 추가
            BoardLike newLike = BoardLike.builder()
                    .user(user)
                    .board(board)
                    .createdAt(LocalDateTime.now())
                    .build();
            boardLikeRepository.save(newLike);
            message = "게시판 좋아요 성공";
            log.info("게시글 좋아요 추가 - boardId: {}, user: {}", boardId, userName);
        }

        return message;
    }
}
