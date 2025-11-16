package com.K_oin.Koin.Controller;

import com.K_oin.Koin.DTO.ApiResponse;
import com.K_oin.Koin.DTO.s3DTOs.PresignedRequest;
import com.K_oin.Koin.DTO.s3DTOs.PresignedResponse;
import com.K_oin.Koin.DTO.s3DTOs.PresignedResponseList;
import com.K_oin.Koin.Service.awsService.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.K_oin.Koin.Service.awsService.S3Service.PresignedUploadResult;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/management/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned")
    public ResponseEntity<ApiResponse<PresignedResponse>> presigned(
            @RequestParam String folder,
            @RequestParam String filename
    )
    {
        try
        {
            PresignedUploadResult uploadResult = s3Service.generatePresignedUploadUrl(folder, filename, Duration.ofMinutes(5));

            ApiResponse<PresignedResponse> successBody = new ApiResponse<>(
                    true,
                    new PresignedResponse(uploadResult.presignedUrl(), uploadResult.key(), filename),
                    "presigned url 발급 성공"
            );
            return ResponseEntity.ok(successBody);
        }
        catch (Exception e)
        {
            ApiResponse<PresignedResponse> errorBody = new ApiResponse<>(
                    false,
                    null,
                    "presigned url 발급 실패: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorBody);
        }
    }

    @PostMapping("/presigned/bulk")
    public ResponseEntity<ApiResponse<PresignedResponseList>> presignedBulk(
            @RequestBody PresignedRequest request
    )
    {
        try {
            List<PresignedResponse> responses = request.getFilenames().stream()
                    .map(name -> {
                        PresignedUploadResult uploadResult = s3Service.generatePresignedUploadUrl(
                                request.getFolder(),
                                name,
                                Duration.ofMinutes(5)
                        );
                        return new PresignedResponse(
                                uploadResult.presignedUrl(),
                                uploadResult.key(),
                                name
                        );
                    })
                    .toList();

            ApiResponse<PresignedResponseList> successBody = new ApiResponse<>(
                    true,
                    new PresignedResponseList(responses),
                    "presigned url 목록 발급 성공"
            );

            return ResponseEntity.ok(successBody);
        }
        catch (Exception e)
        {
            ApiResponse<PresignedResponseList> errorBody = new ApiResponse<>(
                    false,
                    null,
                    "presigned url 목록 발급 실패: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorBody);
        }

    }
}
