package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Builder
public record AppraisalResponse(
        String title,
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImageUrl,
        LocalDate createdAt
) {

    public static AppraisalResponse from(AnalysisResult analysisResult) {
        log.info("analysisResult createdAt: {}", analysisResult.getCreatedAt());
        return AppraisalResponse.builder()
                .title(analysisResult.getTitle())
                .inclination(analysisResult.getInclination())
                .pressure(analysisResult.getPressure())
                .similarity(analysisResult.getSimilarity())
                .verificationImageUrl(analysisResult.getVerificationImgUrl())
                .createdAt(analysisResult.getCreatedAt() != null ? analysisResult.getCreatedAt().toLocalDate() : null)
                .build();
    }
}
