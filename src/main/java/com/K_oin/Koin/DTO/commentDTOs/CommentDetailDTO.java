package com.K_oin.Koin.DTO.commentDTOs;

import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentDetailDTO {
    private Long commentId;
    private LocalDateTime createdDate;
    private BoardAuthorDTO author;
    private boolean anonymous;
    private String body;
    private int likeCount;
    private boolean isMine;
    private List<ReplyCommentDetailDTO> replies;
}
