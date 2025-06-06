package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AppraisalAIResponse(
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImageUrl
) {

    public static AppraisalAIResponse from(AnalysisResult analysisResult) {
        return AppraisalAIResponse.builder()
                .inclination(analysisResult.getInclination())
                .pressure(analysisResult.getPressure())
                .similarity(analysisResult.getSimilarity())
                .verificationImageUrl(analysisResult.getVerificationImgUrl())
                .build();
    }

    public AnalysisResult toEntity() {
        return AnalysisResult.builder()
                .inclination(this.inclination)
                .similarity(this.similarity)
                .pressure(this.pressure)
                .verificationImgUrl(this.verificationImageUrl)
                .build();
    }
}
