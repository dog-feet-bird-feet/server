package com.capstone.dfbf.api.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class LoginReqDto {

    @Email(message = "이메일 형식으로 적어주세요.")
    private String email;
    private String password;
}
