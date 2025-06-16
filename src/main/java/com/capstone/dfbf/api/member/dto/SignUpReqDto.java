package com.capstone.dfbf.api.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpReqDto {
    @Email(message = "이메일 형식으로 작성해주세요")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{10,}$",
            message = "비밀번호는 10자 이상이며 특수문자를 하나 이상 포함해야 합니다."
    )
    private String password;
}
