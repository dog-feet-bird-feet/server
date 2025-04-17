package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HistoryResultResponse(
        String id,
        String name,
        LocalDate createdAt,
        String verificationImgUrl
) {

    public static HistoryResultResponse from(AnalysisResult result) {
        return HistoryResultResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .verificationImgUrl(result.getVerificationImgUrl())
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt().toLocalDate() : null)
                .build();
    }
}
