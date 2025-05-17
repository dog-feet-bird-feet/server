package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AppraisalResponse(
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImageUrl,
        LocalDate createdAt
) {

    public static AppraisalResponse from(AnalysisResult analysisResult) {
        return AppraisalResponse.builder()
                .inclination(analysisResult.getInclination())
                .pressure(analysisResult.getPressure())
                .similarity(analysisResult.getSimilarity())
                .verificationImageUrl(analysisResult.getVerificationImgUrl())
                .createdAt(analysisResult.getCreatedAt() != null ? analysisResult.getCreatedAt().toLocalDate() : null)
                .build();
    }
}
