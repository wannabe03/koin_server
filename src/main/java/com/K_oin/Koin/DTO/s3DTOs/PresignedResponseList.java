package com.K_oin.Koin.DTO.s3DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "presigned url 응답 DTO 리스트")
public class PresignedResponseList {
    private List<PresignedResponse> urls;
}
