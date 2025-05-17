package com.capstone.dfbf.global.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

import static com.capstone.dfbf.global.properties.S3Properties.BUCKET_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Transactional
    public String upload(MultipartFile file, String key) throws IOException {
        uploadToS3Bucket(file, key);
        return getObjectUrl(key);
    }

    private String getObjectUrl(String key) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build()).toString();
    }

    public String generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = buildGetObjectRequest(key);
        GetObjectPresignRequest presignRequest = buildGetObjectPresignRequest(getObjectRequest);
        PresignedGetObjectRequest presignedRequest = getObjectRequest(presignRequest);
        return presignedRequest.url().toString();
    }

    private GetObjectRequest buildGetObjectRequest(String key) {
        return GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
    }

    private GetObjectPresignRequest buildGetObjectPresignRequest(GetObjectRequest getObjectRequest) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();
    }

    private PresignedGetObjectRequest getObjectRequest(GetObjectPresignRequest presignRequest){
        return s3Presigner.presignGetObject(presignRequest);
    }

    private void uploadToS3Bucket(MultipartFile file, String newKey) throws IOException {
        PutObjectRequest putRequest = buildPutObjectRequest(newKey, file.getContentType());
        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    private PutObjectRequest buildPutObjectRequest(String newKey, String fileType) {
        return PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(newKey)
                .contentType(fileType)
                .contentDisposition("inline")
                .build();
    }
}
