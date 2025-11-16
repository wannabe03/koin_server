package com.K_oin.Koin.Service.awsService;

import com.K_oin.Koin.DTO.s3DTOs.PresignedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${AWS_BUCKET_NAME}")
    private String bucket;

    /**
     * Presigned PUT URL 생성 (클라이언트가 직접 PUT으로 업로드할 때 사용)
     */
    public PresignedUploadResult generatePresignedUploadUrl(String folder, String originalFilename,Duration expiresIn) {
        String key = folder + "/" + UUID.randomUUID() + "-" + originalFilename;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(expiresIn)
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return new PresignedUploadResult(presignedRequest.url().toString(), key, null);
    }

    /**
     * 서버에서 바로 업로드(테스트/보조용). production에서는 presigned 방식 권장.
     */
    public String uploadBytes(byte[] bytes, String key, String contentType) {
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(put, RequestBody.fromBytes(bytes));
        return getObjectUrl(key);
    }

    /**
     * S3에 저장된 객체의 public URL (버킷 퍼블릭이거나 CloudFront 등으로 노출된 경우 사용).
     */
    public String getObjectUrl(String key) {
        GetUrlRequest req = GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3Client.utilities().getUrl(req).toString();
    }

    // DTO for single result
    public record PresignedUploadResult(String presignedUrl, String key, String objectUrl) {}
}
