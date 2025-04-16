package com.capstone.dfbf.global.token.vo;

public record AccessTokenVO(
        String token
) {
    public static AccessTokenVO of(String token) {
        return new AccessTokenVO(token);
    }
}