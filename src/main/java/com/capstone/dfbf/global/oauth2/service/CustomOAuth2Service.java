package com.capstone.dfbf.global.oauth2.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.global.oauth2.domain.OAuth2Attributes;
import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import com.capstone.dfbf.global.oauth2.userInfo.OAuth2UserInfo;
import com.capstone.dfbf.global.oauth2.utils.OAuth2Utils;
import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info(oAuth2User.getAttributes().toString());
        OAuthProviderType type = OAuth2Utils.getOAuthProviderType(getRegistrationId(userRequest));
        OAuth2Attributes oauthAttributes = OAuth2Attributes.of(
                type,
                getUserNameAttributeName(userRequest),
                attributes
        );
        OAuth2UserInfo oAuth2UserInfo = oauthAttributes.getOauth2UserInfo();
        Member member = findMember(oAuth2UserInfo);
        memberRepository.save(member);
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        return PrincipalDetails.from(
                authenticatedMember,
                attributes
        );
    }

    private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
        return userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
    }

    private String getRegistrationId(OAuth2UserRequest userRequest) {
        return userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase();
    }

    private Member findMember(OAuth2UserInfo userInfo) {
        return memberRepository
                .findByOauthIdAndProviderType(userInfo.getOAuthId(), userInfo.getProviderType())
                .orElse(Member.createMemberGuest(userInfo));
    }
}

