package com.capstone.dfbf.api.personality.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.personality.domain.Personality;
import com.capstone.dfbf.api.personality.dto.PersonalityAIResponse;
import com.capstone.dfbf.api.personality.dto.PersonalityRequest;
import com.capstone.dfbf.api.personality.dto.PersonalityResponse;
import com.capstone.dfbf.api.personality.error.PersonalityError;
import com.capstone.dfbf.api.personality.error.PersonalityException;
import com.capstone.dfbf.api.personality.repository.PersonalityRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalAIResponse;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.capstone.dfbf.global.exception.error.ErrorCode.MEMBER_NOT_FOUND;
import static com.capstone.dfbf.global.properties.S3Properties.FILE_LIMIT_SIZE;
import static com.capstone.dfbf.global.properties.S3Properties.PERSONALITY_PREFIX;
import static com.capstone.dfbf.global.s3.S3FileNameUtil.renameFilename;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalityService {

    private final static String fastApiEndpoint = "http://localhost:8000/api/v1/personality/analyze";

    private final S3Service s3Service;
    private final RestTemplate restTemplate;
    private final PersonalityRepository personalityRepository;

    @Transactional
    public String uploadImage(MultipartFile file) throws IOException {
        validateFile(file);
        String newKey = PERSONALITY_PREFIX + renameFilename(file.getOriginalFilename());
        return s3Service.upload(file, newKey);
    }

    public PersonalityResponse evaluate(PersonalityRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonalityRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<PersonalityAIResponse> response =
                restTemplate.exchange(fastApiEndpoint, HttpMethod.POST, requestEntity, PersonalityAIResponse.class);

        PersonalityAIResponse personalityResponse = response.getBody();

        assert personalityResponse != null;
        Personality savedPersonality = save(personalityResponse);
        log.info(Objects.requireNonNull(personalityResponse).toString());

        return PersonalityResponse.from(savedPersonality);
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

    private Personality save(final PersonalityAIResponse response) {
        Personality personality = response.toEntity();
        return personalityRepository.save(personality);
    }
}
