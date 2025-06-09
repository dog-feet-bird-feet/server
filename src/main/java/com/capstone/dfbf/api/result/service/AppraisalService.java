package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalAIResponse;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

import static com.capstone.dfbf.global.exception.error.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppraisalService {

    private final static String fastApiEndpoint = "http://13.124.91.167:8000/api/v1/analyze";

    private final MemberRepository memberRepository;
    private final ResultRepository resultRepository;

    private final WebClient webClient;

    public Mono<AppraisalResponse> appraise(final long memberId, final AppraisalRequest request) {
        return webClient.post()
                .uri(fastApiEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AppraisalAIResponse.class)
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> saveAppraisal(memberId, response))
                .map(AppraisalResponse::from);
    }

    public AnalysisResult saveAppraisal(long memberId, AppraisalAIResponse response) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> BaseException.from(MEMBER_NOT_FOUND));
        AnalysisResult result = response.toEntity();
        result.update(member);
        resultRepository.save(result);
        return result;
    }
}
