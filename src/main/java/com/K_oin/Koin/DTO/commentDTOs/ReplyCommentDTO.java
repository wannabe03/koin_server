package com.K_oin.Koin.DTO.commentDTOs;

import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "댓글의 답글 생성 DTO")
public class ReplyCommentDTO {

    @Schema(description = "답글이 달릴 댓글의 ID", example = "10")
    private Long commentId;

    @Schema(description = "익명 여부", example = "false")
    private boolean anonymous;

    @Schema(description = "답글 내용", example = "저도 동의합니다")
    private String body;
}
