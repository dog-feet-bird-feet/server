package com.capstone.dfbf.api.member.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.dto.LoginReqDto;
import com.capstone.dfbf.api.member.dto.SignUpReqDto;
import com.capstone.dfbf.api.member.error.MemberError;
import com.capstone.dfbf.api.member.error.MemberException;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import com.capstone.dfbf.global.token.vo.AccessTokenVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public AccessTokenVO login(LoginReqDto request) {
        Member member = loadMemberOrThrow(request);
        validatePassword(request, member);
        return jwtProvider.generateAccessToken(member);
    }

    @Transactional
    public void signup(SignUpReqDto request) {
        validateMemberExistByEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = Member.create(request.getEmail(), encodedPassword);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void validateEmail(String email){
        validateMemberExistByEmail(email);
    }

    private Member loadMemberOrThrow(LoginReqDto request) {
        return memberRepository.findMemberByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(MemberError.NON_EXIST_MEMBER));
    }

    private void validatePassword(LoginReqDto request, Member member) {
        if (!member.isPasswordMatch(passwordEncoder, request.getPassword())) {
            throw new MemberException(MemberError.INVALID_PASSWORD);
        }
    }

    private void validateMemberExistByEmail(String email) {
        if(memberRepository.existsByEmail(email)){
            throw MemberException.from(MemberError.ALREADY_EXIST_EMAIL);
        }
    }
}
