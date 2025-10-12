package com.K_oin.Koin.DTO.boardDTOs;

import com.K_oin.Koin.DTO.commentDTOs.CommentDetailDTO;
import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardDetailDTO {
    private Long boardId;         // 게시글 ID
    private String title;         // 제목
    private String body;          // 본문
    private boolean anonymous;      // 익명 여부
    private LocalDateTime createdAt; // 생성일
    private int likeCount;        // 좋아요 수
    private int commentCount;     // 댓글 수
    private BoardAuthorDTO boardAuthorDTO;
    private List<CommentDetailDTO> comments; // 댓글 목록
}
