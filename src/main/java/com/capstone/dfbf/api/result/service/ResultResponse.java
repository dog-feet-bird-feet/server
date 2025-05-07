package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {

    public static ResultResponse from(AnalysisResult result) {
        return ResultResponse.builder()
                .id(result.getId())
                .name(result.getTitle())
                .similarity(result.getSimilarity())
                .pressure(result.getPressure())
                .inclination(result.getInclination())
                .verificationImgUrl(result.getVerificationImgUrl())
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt() : null)
                .build();
    }
}
