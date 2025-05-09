package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.api.result.dto.AppraisalSuccess;
import com.capstone.dfbf.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.capstone.dfbf.global.exception.error.ErrorCode.RESULT_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppraisalService {

    private final static String fastApiEndpoint = "http://127.0.0.1:8000/generate";

    private final ResultRepository resultRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public AppraisalResponse appraise(final AppraisalRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AppraisalRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AppraisalResponse> response =
                restTemplate.exchange(fastApiEndpoint, HttpMethod.POST, requestEntity, AppraisalResponse.class);

        AppraisalResponse appraisalResponse = response.getBody();

        assert appraisalResponse != null;
        saveAppraisal(appraisalResponse);
        log.info(Objects.requireNonNull(appraisalResponse).toString());

        return appraisalResponse;
    }

    @Transactional(readOnly = true)
    public AppraisalResponse getAppraisal(final String id) {
        AnalysisResult result = resultRepository.findById(id).orElseThrow(() -> BaseException.from(RESULT_NOT_FOUND));
        return AppraisalResponse.from(result);
    }

    @Transactional
    public void deleteAppraisal(final String id) {
        resultRepository.deleteById(id);
    }

    private void saveAppraisal(final AppraisalResponse response) {
        // TODO 커스텀 예외 처리
        AnalysisResult result = resultRepository.findById(response.id()).orElseThrow(IllegalArgumentException::new);
        result = result.updateWith(response);
        resultRepository.save(result);
    }
}
