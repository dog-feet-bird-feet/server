package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResultResponse(
        String id,
        String name,
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImgUrl,
        LocalDateTime createdAt
) {

    public static ResultResponse from(AnalysisResult result) {
        return ResultResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .similarity(result.getSimilarity())
                .pressure(result.getPressure())
                .inclination(result.getInclination())
                .verificationImgUrl(result.getVerificationImgUrl())
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt() : null)
                .build();
    }
}
