package com.capstone.dfbf.api.member.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.dto.LoginReqDto;
import com.capstone.dfbf.api.member.error.MemberError;
import com.capstone.dfbf.api.member.error.MemberException;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import com.capstone.dfbf.global.token.vo.AccessTokenVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void login(LoginReqDto request, HttpServletResponse response){
        Member member = loadMemberOrRegister(request);
        AccessTokenVO accessToken = jwtProvider.generateAccessToken(member);
        response.setHeader("Authorization", "Bearer " + accessToken.token());
    }

    private Member loadMemberOrRegister(LoginReqDto request){
        return memberRepository.findMemberByEmail(request.getEmail())
                .map(member -> {
                    if (!passwordEncoder.matches(request.getPassword(), member.getEncodedPassword())) {
                        throw new MemberException(MemberError.INVALID_PASSWORD);
                    }
                    return member;
                })
                .orElseGet(() -> {
                    String encodedPassword = passwordEncoder.encode(request.getPassword());
                    Member newMember = Member.createMember(request.getEmail(), encodedPassword);
                    memberRepository.save(newMember);
                    return newMember;
                });
    }
}
