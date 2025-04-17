package com.capstone.dfbf.global.security.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.exception.error.ErrorCode;
import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String email){
        Member findMember = memberRepository
                .findMemberByEmail(email)
                .orElseThrow(() -> BaseException.from(ErrorCode.MEMBER_NOT_FOUND));
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(findMember);
        return PrincipalDetails.from(authenticatedMember);
    }
}
