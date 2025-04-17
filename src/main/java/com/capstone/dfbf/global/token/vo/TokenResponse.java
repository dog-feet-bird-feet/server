package com.capstone.dfbf.global.token.vo;


public record TokenResponse(
        AccessTokenVO accessToken,
        RefreshTokenVO refreshToken
) {
    public static TokenResponse of(AccessTokenVO accessToken, RefreshTokenVO refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}
