package com.capstone.dfbf.api.evidence.service;

import com.capstone.dfbf.api.evidence.domain.Comparison;
import com.capstone.dfbf.api.evidence.domain.Verification;
import com.capstone.dfbf.api.evidence.dto.ComparisonRes;
import com.capstone.dfbf.api.evidence.dto.VerificationRes;
import com.capstone.dfbf.api.evidence.error.EvidenceError;
import com.capstone.dfbf.api.evidence.error.EvidenceException;
import com.capstone.dfbf.api.evidence.repository.ComparisonRepository;
import com.capstone.dfbf.api.evidence.repository.VerificationRepository;
import com.capstone.dfbf.global.exception.error.ErrorCode;
import com.capstone.dfbf.global.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvidenceService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final VerificationRepository verificationRepository;
    private final ComparisonRepository comparisonRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public VerificationRes verificationUpload(MultipartFile file) throws IOException {
        validateFile(file);
        String newKey = S3Properties.VERIFICATION_PREFIX + renameFilename(file.getOriginalFilename());
        uploadToS3Bucket(file, newKey);
        Verification verification = Verification.from(newKey);
        verificationRepository.save(verification);
        return VerificationRes.from(getObjectUrl(newKey));
    }

    @Transactional
    public ComparisonRes comparisonUpload(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        if(files.size() != 5){
            throw EvidenceException.from(EvidenceError.INVALID_FILE_COUNT);
        }
        for (MultipartFile file : files) {
            validateFile(file);
            String newKey = S3Properties.COMPARISON_PREFIX + renameFilename(file.getOriginalFilename());
            uploadToS3Bucket(file, newKey);
            Comparison comparison = Comparison.from(newKey);
            comparisonRepository.save(comparison);
            urls.add(getObjectUrl(newKey));
        }
        return ComparisonRes.from(urls);
    }

    @Transactional(readOnly = true)
    public String getPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = buildGetObjectRequest(key);
        GetObjectPresignRequest presignRequest = buildGetObjectPresignRequest(getObjectRequest);
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw EvidenceException.from(EvidenceError.INVALID_FILE_SIZE);
        }
        String contentType = file.getContentType();
        log.info("File content type is {}", contentType);
        if (!Objects.equals(contentType, "image/jpeg") && !Objects.equals(contentType, "image/png")) {
            throw EvidenceException.from(EvidenceError.INVALID_FILE_TYPE);
        }
    }

    private void uploadToS3Bucket(MultipartFile file, String newKey) throws IOException {
        PutObjectRequest putRequest = buildPutObjectRequest(newKey, file.getContentType());
        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    private PutObjectRequest buildPutObjectRequest(String newKey, String fileType) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(newKey)
                .contentType(fileType)
                .contentDisposition("inline")
                .build();
    }

    private String getObjectUrl(String newKey) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucket)
                .key(newKey)
                .build()).toString();
    }

    private GetObjectPresignRequest buildGetObjectPresignRequest(GetObjectRequest getObjectRequest) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();
    }

    private GetObjectRequest buildGetObjectRequest(String key) {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    private String renameFilename(String fileName){
        return UUID.randomUUID() + "_" + fileName;
    }
}
