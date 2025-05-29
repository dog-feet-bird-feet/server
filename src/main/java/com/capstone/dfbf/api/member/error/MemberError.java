package com.capstone.dfbf.api.member.error;

import com.capstone.dfbf.global.exception.error.BaseCode;
import com.capstone.dfbf.global.exception.error.ErrorDisplayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberError implements BaseCode {

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M000", "비밀번호가 올바르지 않습니다.", ErrorDisplayType.POPUP),
    NON_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "M001", "존재하지 않은 회원입니다.", ErrorDisplayType.POPUP),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
