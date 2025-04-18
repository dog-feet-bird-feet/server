package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.domain.History;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record HistoryResultResponse(
        String id,
        String name,
        LocalDate createdAt,
        String verificationImgUrl
) {

    public static List<HistoryResultResponse> from(History history) {
        return history.getResults().stream().map(HistoryResultResponse::from).toList();
    }

    public static HistoryResultResponse from(AnalysisResult result) {
        return HistoryResultResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .verificationImgUrl(result.getVerificationImgUrl())
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt().toLocalDate() : null)
                .build();
    }
}
