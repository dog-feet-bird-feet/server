package com.capstone.dfbf.global.oauth2.userInfo;


import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import com.capstone.dfbf.global.oauth2.properties.KakaoProperties;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{
    public final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.attributes = (Map<String, Object>) attributes.get(KakaoProperties.KAKAO_INFO_KEY);
    }

    @Override
    public String getOAuthId() {
        return String.valueOf(attributes.get(KakaoProperties.KAKAO_INFO_ID));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get(KakaoProperties.KAKAO_INFO_EMAIL));
    }

    @Override
    public String getName() {
        if (attributes == null) {
            return null;
        }
        Map<String, Object> profile = (Map<String, Object>) attributes.get(KakaoProperties.KAKAO_PROFILE);

        return profile.get(KakaoProperties.KAKAO_INFO_NAME).toString();
    }

    @Override
    public String getPhoneNum() {
        return "";
    }

    @Override
    public OAuthProviderType getProviderType() {
        return OAuthProviderType.KAKAO;
    }
}
