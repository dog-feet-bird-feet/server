package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.domain.History;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record HistoryResultResponse(
        String id,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
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
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt() : null)
                .build();
    }
}
