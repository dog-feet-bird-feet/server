package com.capstone.dfbf.api.member;

import com.capstone.dfbf.global.base.BaseEntity;
import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import com.capstone.dfbf.global.oauth2.userInfo.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String oauthId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private OAuthProviderType providerType;

    public enum Role {
        USER("USER"),;

        Role(String type) {}

        private String type;
    }

    public static Member createMemberGuest(OAuth2UserInfo userInfo){
        return Member.builder()
                .nickname(userInfo.getName())
                .email(userInfo.getEmail())
                .providerType(userInfo.getProviderType())
                .oauthId(userInfo.getOAuthId())
                .role(Role.USER)
                .build();
    }
}
