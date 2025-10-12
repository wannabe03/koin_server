package com.K_oin.Koin.DTO.boardDTOs;

import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardSummaryDTO {
    private Long boardId;         // 게시글 ID
    private String title;         // 제목
    private String preview;       // 본문 일부 (ex: 100자 이하)
    private LocalDateTime createdAt; // 생성일
    private int likeCount;        // 좋아요 수
    private int commentCount;     // 댓글 수
    private BoardAuthorDTO boardAuthorDTO;
}
