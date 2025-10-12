package com.K_oin.Koin.DTO.userDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "사용자 정보를 전달하는 DTO")
public class UserDTO {

    @NotBlank
    @Schema(description = "사용자 이름 (실명)", example = "홍길동")
    private String name;

    @NotBlank
    @Schema(description = "로그인용 아이디", example = "hong123")
    private String username;

    @NotBlank
    @Schema(description = "사용자 이메일", example = "hong@example.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호 (암호화되어 저장됨)", example = "P@ssw0rd!")
    private String password;

    @NotBlank
    @Schema(description = "사용자 별명(닉네임)", example = "길동이")
    private String nickname;

    @Schema(description = "국적 (예: KOREA, USA, JAPAN 등)", example = "KOREA")
    private String nationality;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "생년월일", example = "1995-07-20")
    private LocalDate birthDate;
}
