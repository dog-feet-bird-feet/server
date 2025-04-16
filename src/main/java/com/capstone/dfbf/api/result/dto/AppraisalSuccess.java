package com.capstone.dfbf.api.result.dto;

import lombok.Builder;

@Builder
public record AppraisalSuccess(
        boolean isSuccess,
        String message
) {

    public static AppraisalSuccess from(final String message) {
        return AppraisalSuccess.builder()
                .isSuccess(true)
                .message(message)
                .build();
    }
}
