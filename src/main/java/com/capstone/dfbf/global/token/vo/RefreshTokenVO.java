package com.capstone.dfbf.global.token.vo;

public record RefreshTokenVO(
        String token
) {
    public static RefreshTokenVO of(String token) {
        return new RefreshTokenVO(token);
    }
}

