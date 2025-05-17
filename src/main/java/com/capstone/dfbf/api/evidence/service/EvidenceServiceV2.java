package com.capstone.dfbf.api.evidence.service;

import com.capstone.dfbf.api.evidence.domain.Comparison;
import com.capstone.dfbf.api.evidence.domain.Verification;
import com.capstone.dfbf.api.evidence.error.EvidenceError;
import com.capstone.dfbf.api.evidence.error.EvidenceException;
import com.capstone.dfbf.api.evidence.repository.ComparisonRepository;
import com.capstone.dfbf.api.evidence.repository.VerificationRepository;
import com.capstone.dfbf.global.properties.S3Properties;
import com.capstone.dfbf.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.capstone.dfbf.global.properties.S3Properties.FILE_LIMIT_SIZE;
import static com.capstone.dfbf.global.s3.S3FileNameUtil.renameFilename;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvidenceServiceV2 {

    private final S3Service s3Service;
    private final VerificationRepository verificationRepository;
    private final ComparisonRepository comparisonRepository;

    @Transactional
    public String uploadVerification(MultipartFile file) throws IOException {
        return uploadAndSave(
                file,
                S3Properties.VERIFICATION_PREFIX,
                Verification::from,
                verificationRepository::save
        );
    }

    @Transactional
    public List<String> comparisonUpload(List<MultipartFile> files) throws IOException {
        validateImageCounts(files);
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadAndSave(
                    file,
                    S3Properties.COMPARISON_PREFIX,
                    Comparison::from,
                    comparisonRepository::save
            );
            urls.add(url);
        }
        return urls;
    }

    @Transactional(readOnly = true)
    public String getPresignedUrl(String key){
        return s3Service.generatePresignedUrl(key);
    }

    private <T> String uploadAndSave(MultipartFile file, String prefix, Function<String, T> entityFactory, Consumer<T> saveFunction) throws IOException {
        validateFile(file);
        String newKey = prefix + renameFilename(file.getOriginalFilename());
        String url = s3Service.upload(file, newKey);
        T entity = entityFactory.apply(newKey);
        saveFunction.accept(entity);
        return url;
    }

    private void validateImageCounts(List<MultipartFile> files) {
        if(files.size() != 5){
            throw EvidenceException.from(EvidenceError.INVALID_FILE_COUNT);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > FILE_LIMIT_SIZE) {
            throw EvidenceException.from(EvidenceError.INVALID_FILE_SIZE);
        }
        String contentType = file.getContentType();
        log.info("File content type is {}", contentType);
        if (!Objects.equals(contentType, "image/jpeg") && !Objects.equals(contentType, "image/png")) {
            throw EvidenceException.from(EvidenceError.INVALID_FILE_TYPE);
        }
    }
}
