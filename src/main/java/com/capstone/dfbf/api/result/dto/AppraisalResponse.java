package com.capstone.dfbf.api.result.dto;

import com.capstone.dfbf.api.result.domain.AnalysisResult;

public record AppraisalResponse(
        Long id,
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImgUrl
) {
}
