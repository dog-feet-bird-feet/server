package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalAIResponse;
import com.capstone.dfbf.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.capstone.dfbf.global.exception.error.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppraisalService {

    private final static String fastApiEndpoint = "http://13.124.91.167:8000/api/v1/analyze";

    private final MemberRepository memberRepository;
    private final ResultRepository resultRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public AppraisalAIResponse appraise(final long memberId, final AppraisalRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AppraisalRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AppraisalAIResponse> response =
                restTemplate.exchange(fastApiEndpoint, HttpMethod.POST, requestEntity, AppraisalAIResponse.class);

        AppraisalAIResponse appraisalResponse = response.getBody();

        assert appraisalResponse != null;
        saveAppraisal(memberId, appraisalResponse);
        log.info(Objects.requireNonNull(appraisalResponse).toString());

        return appraisalResponse;
    }

    private void saveAppraisal(final long memberId, final AppraisalAIResponse response) {
        // TODO 커스텀 예외 처리

        Member member = memberRepository.findById(memberId).orElseThrow(() -> BaseException.from(MEMBER_NOT_FOUND));
        AnalysisResult result = response.toEntity();
        result.update(member);
        resultRepository.save(result);
    }
}
