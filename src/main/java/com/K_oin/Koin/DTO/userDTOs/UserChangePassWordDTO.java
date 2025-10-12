package com.K_oin.Koin.DTO.userDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "비밀번호 변경 DTO")
public class UserChangePassWordDTO {

    @Schema(description = "새로 변경할 비밀번호", example = "NewP@ssw0rd!")
    private String newPassword;
}
