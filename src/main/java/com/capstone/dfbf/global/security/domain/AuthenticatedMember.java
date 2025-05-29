package com.capstone.dfbf.global.security.domain;

import com.capstone.dfbf.api.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class AuthenticatedMember {
    private Long memberId;
    private String email;

    public static AuthenticatedMember from(Member member){
        return new AuthenticatedMember(
                member.getId(),
                member.getEmail()
        );
    }
}
