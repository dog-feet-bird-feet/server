package com.capstone.dfbf.global.oauth2.domain;

import lombok.Getter;

@Getter
public enum OAuthProviderType {
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String provider;

    OAuthProviderType(String provider) {
        this.provider = provider;
    }
}