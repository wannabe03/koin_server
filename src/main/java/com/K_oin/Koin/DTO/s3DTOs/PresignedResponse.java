package com.K_oin.Koin.DTO.s3DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "presigned url 응답 DTO")
public class PresignedResponse {
    private String presignedUrl;
    private String key;
    private String originalFilename;  // 어떤 파일인지 식별용
}
