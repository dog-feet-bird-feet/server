package com.capstone.dfbf.api.result.dto;

public record AppraisalResponse(
        Long id,
        Double similarity,
        Double pressure,
        Double inclination,
        String verificationImgUrl
) {
}
