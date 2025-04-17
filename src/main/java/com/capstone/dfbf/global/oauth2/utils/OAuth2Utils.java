package com.capstone.dfbf.global.oauth2.utils;

import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import com.capstone.dfbf.global.oauth2.userInfo.KakaoOAuth2UserInfo;
import com.capstone.dfbf.global.oauth2.userInfo.NaverOAuth2UserInfo;
import com.capstone.dfbf.global.oauth2.userInfo.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OAuth2Utils {

    public static OAuthProviderType getOAuthProviderType(String registrationId) {
        if ("NAVER".equals(registrationId)) {
            return OAuthProviderType.NAVER;
        }
        else if ("KAKAO".equals(registrationId)) {
            return OAuthProviderType.KAKAO;
        }
        return null;
    }

    public static OAuth2UserInfo getOAuth2UserInfo(OAuthProviderType socialType,
                                                   Map<String, Object> attributes) {


        if (OAuthProviderType.NAVER.equals(socialType)) {
            return new NaverOAuth2UserInfo(attributes);
        }
        else if (OAuthProviderType.KAKAO.equals(socialType)) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        return null;
    }
}
