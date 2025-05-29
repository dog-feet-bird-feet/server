package com.capstone.dfbf.api.member.controller;

import com.capstone.dfbf.api.member.dto.LoginReqDto;
import com.capstone.dfbf.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public void login(@RequestBody LoginReqDto reqDto, HttpServletResponse response) {
        memberService.login(reqDto, response);
    }
}
