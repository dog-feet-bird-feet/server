package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.domain.History;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "히스토리 조회 응답")
@Builder
public record HistoryResultResponse(
        @Schema(description = "히스토리 ID", example = "1")
        String id,
        @Schema(description = "히스토리 이름", example = "제목없음")
        String name,
        @Schema(description = "히스토리 생성일", example = "2025-05-15T13:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        @Schema(description = "히스토리 생성일", example = "https://s3.aws.com?q=dawdadwadaw")
        String verificationImgUrl
) {

    public static List<HistoryResultResponse> from(History history) {
        return history.getResults().stream().map(HistoryResultResponse::from).toList();
    }

    public static HistoryResultResponse from(AnalysisResult result) {
        return HistoryResultResponse.builder()
                .id(result.getId())
                .name(result.getTitle())
                .verificationImgUrl(result.getVerificationImgUrl())
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt() : null)
                .build();
    }
}
