package com.K_oin.Koin.DTO.s3DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "presigned url 발급 DTO")
public class PresignedRequest {
    private String folder;
    private List<String> filenames;
}
