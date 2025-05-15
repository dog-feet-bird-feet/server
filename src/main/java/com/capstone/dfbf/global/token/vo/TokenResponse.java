package com.capstone.dfbf.global.token.vo;


public record TokenResponse(
        AccessTokenVO accessToken
) {
    public static TokenResponse of(AccessTokenVO accessToken) {
        return new TokenResponse(accessToken);
    }
}
