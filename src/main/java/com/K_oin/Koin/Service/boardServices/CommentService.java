package com.K_oin.Koin.Service.boardServices;

import com.K_oin.Koin.DTO.commentDTOs.CommentDTO;
import com.K_oin.Koin.DTO.commentDTOs.CommentDetailDTO;
import com.K_oin.Koin.DTO.commentDTOs.ReplyCommentDTO;
import com.K_oin.Koin.DTO.commentDTOs.ReplyCommentDetailDTO;
import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import com.K_oin.Koin.Entitiy.BoardEntity.*;
import com.K_oin.Koin.Entitiy.BoardEntity.Likes.BoardCommentLike;
import com.K_oin.Koin.Entitiy.BoardEntity.Likes.CommentReplyLike;
import com.K_oin.Koin.Repository.boardRepository.*;
import com.K_oin.Koin.Repository.boardRepository.Likes.BoardCommentLikeRepository;
import com.K_oin.Koin.Repository.boardRepository.Likes.CommentReplyLikeRepository;
import com.K_oin.Koin.Repository.userRepository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final BoardCommentLikeRepository boardCommentLikeRepository;
    private final CommentReplyLikeRepository commentReplyLikeRepository;

    public void createComment(CommentDTO commentDTO, String userName) {
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + commentDTO.getBoardId()));

        BoardComment comment;

        try {
            comment = BoardComment.builder()
                    .board(board)
                    .author(user)
                    .content(commentDTO.getBody())
                    .createdAt(LocalDateTime.now())
                    .anonymous(commentDTO.isAnonymous()) // 기본값 설정, 필요에 따라 DTO에 추가 가능
                    .build();

            commentRepository.save(comment);
            log.info("댓글 생성 성공 - boardId: {}, title: {}", board.getBoardId(), board.getTitle());
        } catch (Exception e) {
            log.error("댓글 생성 실패 - 사용자: {}, 오류: {}", userName, e.getMessage());
            throw new RuntimeException("게시글 생성 중 오류 발생: " + e.getMessage(), e);
        }

    }

    public void createReplyComment(ReplyCommentDTO replyCommentDTO, String userName) {
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        BoardComment boardComment = commentRepository.findById(replyCommentDTO.getCommentId())
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + replyCommentDTO.getCommentId()));

        CommentReply commentReply;

        try {
            commentReply = CommentReply.builder()
                    .parentComment(boardComment)
                    .author(user)
                    .content(replyCommentDTO.getBody())
                    .createdAt(LocalDateTime.now())
                    .anonymous(replyCommentDTO.isAnonymous()) // 기본값 설정, 필요에 따라 DTO에 추가 가능
                    .build();

            commentReplyRepository.save(commentReply);
            log.info("대댓글 생성 성공 - commentId: {}, boardId: {}", boardComment.getCommentId(), boardComment.getBoard().getBoardId());
        } catch (Exception e) {
            log.error("대댓글 생성 실패 - 사용자: {}, 오류: {}", userName, e.getMessage());
            throw new RuntimeException("대댓글 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public List<ReplyCommentDetailDTO> getCommentReply(Long commentId) {
        BoardComment comment = commentRepository.findWithRepliesAndLikesByCommentId(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다 댓글id: " + commentId));

        return comment.getReplies().stream()
                .map(reply -> {
                    BoardAuthorDTO authorDTO = null;

                    // 익명 댓글이 아닐 경우 작성자 정보 세팅
                    if (!reply.isAnonymous()) {
                        authorDTO = BoardAuthorDTO.builder()
                                .nickname(reply.getAuthor().getNickname())
                                .nationality(reply.getAuthor().getNationality().name())
                                .build();
                    }

                    return ReplyCommentDetailDTO.builder()
                            .commentId(comment.getCommentId())
                            .replyCommentId(reply.getCommentReplyId())
                            .author(authorDTO)
                            .body(reply.getContent())
                            .anonymous(reply.isAnonymous())
                            .createdDate(reply.getCreatedAt())
                            .likeCount(reply.getLikes().size())
                            .build();
                })
                .toList();
    }

    public void deleteComment(Long commentId, String userName) {

        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        BoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));

        if (!comment.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.(회원정보 불일치)");
        }

        try {
            commentRepository.delete(comment);
            log.info("댓글 삭제 성공 - commentId: {}, boardId: {}", comment.getCommentId(), comment.getBoard().getBoardId());
        } catch (Exception e) {
            log.error("댓글 삭제 실패 - 사용자: {}, 오류: {}", userName, e.getMessage());
            throw new RuntimeException("댓글 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public void deleteReplyComment(Long replyCommentId, String userName) {
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        CommentReply commentReply = commentReplyRepository.findById(replyCommentId)
                .orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다: " + replyCommentId));

        if (!commentReply.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.(회원정보 불일치)");
        }

        try {
            commentReplyRepository.delete(commentReply);
            log.info("대댓글 삭제 성공 - commentReplyId: {}, boardId: {}", commentReply.getCommentReplyId(), commentReply.getParentComment().getBoard().getBoardId());
        } catch (Exception e) {
            log.error("댓글 삭제 실패 - 사용자: {}, 오류: {}", userName, e.getMessage());
            throw new RuntimeException("댓글 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public String likeComment(Long commentId, String userName) {
        String message = "";

        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        BoardComment boardComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        BoardCommentLike boardCommentLike = boardCommentLikeRepository.findByUserAndBoardComment(user, boardComment);

        if (boardCommentLike != null) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            boardCommentLikeRepository.delete(boardCommentLike);
            message = "댓글 좋아요 취소 성공";
            log.info("댓글 좋아요 취소 - commentId: {}, user: {}", commentId, userName);
        } else {
            // 좋아요 추가
            BoardCommentLike newLike = BoardCommentLike.builder()
                    .user(user)
                    .boardComment(boardComment)
                    .createdAt(LocalDateTime.now())
                    .build();
            boardCommentLikeRepository.save(newLike);
            message = "댓글 좋아요 성공";
            log.info("댓글 좋아요 추가 - commentId: {}, user: {}", commentId, userName);
        }

        return message;
    }

    public String likeReplyComment(Long replyCommentId, String userName) {
        String message = "";

        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userName));

        CommentReply commentReply = commentReplyRepository.findById(replyCommentId)
                .orElseThrow(() -> new EntityNotFoundException("대댓글을 찾을 수 없습니다."));

        CommentReplyLike commentReplyLike = commentReplyLikeRepository.findByUserAndCommentReply(user, commentReply);

        if (commentReplyLike != null) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            commentReplyLikeRepository.delete(commentReplyLike);
            message = "대댓글 좋아요 취소 성공";
            log.info("대댓글 좋아요 취소 - replyCommentId: {}, user: {}", replyCommentId, userName);
        } else {
            // 좋아요 추가
            CommentReplyLike newLike = CommentReplyLike.builder()
                    .user(user)
                    .commentReply(commentReply)
                    .createdAt(LocalDateTime.now())
                    .build();
            commentReplyLikeRepository.save(newLike);
            message = "대댓글 좋아요 성공";
            log.info("대댓글 좋아요 추가 - replyCommentId: {}, user: {}", replyCommentId, userName);
        }

        return message;
    }
}
