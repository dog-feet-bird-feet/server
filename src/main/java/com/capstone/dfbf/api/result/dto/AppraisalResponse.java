package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AppraisalResponse(
        String id,
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImgUrl,
        LocalDate createdAt
) {

    public static AppraisalResponse from(AnalysisResult analysisResult) {
        return AppraisalResponse.builder()
                .id(analysisResult.getId())
                .inclination(analysisResult.getInclination())
                .pressure(analysisResult.getPressure())
                .similarity(analysisResult.getSimilarity())
                .verificationImgUrl(analysisResult.getVerificationImgUrl())
                .createdAt(analysisResult.getCreatedAt().toLocalDate())
                .build();
    }
}
