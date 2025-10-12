package com.K_oin.Koin.DTO.commentDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "댓글 생성 DTO")
public class CommentDTO {

    @Schema(description = "댓글이 달릴 게시글의 ID", example = "1")
    private Long BoardId;

    @Schema(description = "익명 여부", example = "false")
    private boolean anonymous;

    @Schema(description = "댓글 내용", example = "좋은 글 감사합니다")
    private String body;
}
