package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppraisalService {

    @Value("${ggzz.fastapi.endpoint}")
    private String fastApiEndpoint;

    private final ResultRepository resultRepository;

    public AppraisalResponse appraise(final AppraisalRequest request) {
        RestTemplate restTemplate = new RestTemplate();

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

    private void saveAppraisal(final AppraisalResponse response) {
        // TODO 커스텀 예외 처리
        AnalysisResult result = resultRepository.findById(response.id()).orElseThrow(IllegalArgumentException::new);
        result = result.updateWith(response);
        resultRepository.save(result);
    }
}
