package com.K_oin.Koin.DTO.userDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAuthorDTO {
    private String nickname;
    private String nationality;
}
