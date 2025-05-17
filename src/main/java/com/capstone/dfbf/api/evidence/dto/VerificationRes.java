package com.capstone.dfbf.api.evidence.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationRes {

    private String verificationUrl;

    public static VerificationRes from(String verificationUrl) {
        return new VerificationRes(verificationUrl);
    }
}
