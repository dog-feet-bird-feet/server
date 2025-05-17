package com.capstone.dfbf.api.personality.service;

import com.capstone.dfbf.api.evidence.error.EvidenceError;
import com.capstone.dfbf.api.evidence.error.EvidenceException;
import com.capstone.dfbf.api.personality.error.PersonalityError;
import com.capstone.dfbf.api.personality.error.PersonalityException;
import com.capstone.dfbf.global.properties.S3Properties;
import com.capstone.dfbf.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.capstone.dfbf.global.properties.S3Properties.FILE_LIMIT_SIZE;
import static com.capstone.dfbf.global.properties.S3Properties.PERSONALITY_PREFIX;
import static com.capstone.dfbf.global.s3.S3FileNameUtil.renameFilename;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalityService {

    private final S3Service s3Service;

    @Transactional
    public String evaluatePersonality(MultipartFile file) throws IOException {
        validateFile(file);
        String newKey = PERSONALITY_PREFIX + renameFilename(file.getOriginalFilename());
        return s3Service.upload(file, newKey);
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > FILE_LIMIT_SIZE) {
            throw PersonalityException.from(PersonalityError.INVALID_FILE_SIZE);
        }
        String contentType = file.getContentType();
        if (!Objects.equals(contentType, "image/jpeg") &&
                !Objects.equals(contentType, "image/png")) {
            throw PersonalityException.from(PersonalityError.INVALID_FILE_TYPE);
        }
    }
}
