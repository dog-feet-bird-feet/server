package com.capstone.dfbf.api.result.dto;

import lombok.Builder;

@Builder
public record AppraisalSuccess(
        boolean isSuccess,
        Object message
) {

    public static AppraisalSuccess from(final Object message) {
        return AppraisalSuccess.builder()
                .isSuccess(true)
                .message(message)
                .build();
    }
}
