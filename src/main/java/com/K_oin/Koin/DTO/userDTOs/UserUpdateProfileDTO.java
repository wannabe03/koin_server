package com.K_oin.Koin.DTO.userDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "프로필 수정 요청 DTO")
public class UserUpdateProfileDTO {
    @Schema(description = "변경할 닉네임", example = "new_nickname")
    private String nickname;

    @Schema(description = "변경할 국적", example = "KOREA")
    private String nationality;

    @Schema(description = "변경할 생년월일", example = "1990-01-01")
    private LocalDate birthDate;
}
