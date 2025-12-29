package com.capstone.dfbf.api.member.controller;

import com.capstone.dfbf.api.member.dto.LoginReqDto;
import com.capstone.dfbf.api.member.dto.SignUpReqDto;
import com.capstone.dfbf.api.member.service.MemberService;
import com.capstone.dfbf.global.token.vo.AccessTokenVO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginReqDto reqDto, HttpServletResponse response) {
        AccessTokenVO accessToken = memberService.login(reqDto);
        response.setHeader("Authorization", "Bearer " + accessToken.token());
    }

    @GetMapping("/check-email")
    public void checkEmail(@RequestParam String email) {
        memberService.validateEmail(email);
    }

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignUpReqDto request){
        memberService.signup(request);
    }
}
