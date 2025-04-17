package com.capstone.dfbf.global.oauth2.userInfo;

import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import com.capstone.dfbf.global.oauth2.properties.NaverProperties;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public Map<String, Object> attributes;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.attributes = (Map<String, Object>) attributes.get(NaverProperties.NAVER_INFO_KEY);
    }

    @Override
    public String getOAuthId() {
        return String.valueOf(attributes.get(NaverProperties.NAVER_INFO_ID));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get(NaverProperties.NAVER_INFO_EMAIL));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get(NaverProperties.NAVER_INFO_NAME));
    }

    @Override
    public String getPhoneNum() {
        return String.valueOf(attributes.get(NaverProperties.NAVER_INFO_MOBILE));
    }

    @Override
    public OAuthProviderType getProviderType() {
        return OAuthProviderType.NAVER;
    }
}
