package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.api.result.service.AppraisalService;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "감정 분석 기능", description = "감정 분석 과정에서 수행되는 API")
@Slf4j
@AllArgsConstructor
@RequestMapping
@RestController
public class AppraisalController {

    private AppraisalService appraisalService;

    @Operation(summary = "감정하기", description = "감정결과ID와 이미지URL을 AI 서버에 전송하여 감정결과를 받습니다.")
    @PostMapping("/appraisal")
    public Mono<ResponseEntity<AppraisalResponse>> appraisal(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody AppraisalRequest request) {
        return appraisalService.appraise(principalDetails.getAuthenticatedMember().getMemberId(), request)
                .map(ResponseEntity::ok);
    }
}
