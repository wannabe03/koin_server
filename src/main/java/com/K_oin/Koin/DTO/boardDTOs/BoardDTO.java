package com.K_oin.Koin.DTO.boardDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "게시글 생성 DTO")
public class BoardDTO {

    @Schema(description = "게시글 제목", example = "공지사항입니다")
    private String title;         // 제목

    @Schema(description = "게시글 본문", example = "내용을 입력하세요")
    private String body;

    @Schema(description = "게시판 유형 (예: 자유게시판, 공지사항 등)", example = "FREEBOARD(EnumData의 BoardType 참고)")
    private String boardType;    // 게시판 유형 (예: 자유게시판, 공지사항 등)

    @Schema(description = "익명 여부", example = "false")
    private boolean anonymous;   // 익명 여부
}
