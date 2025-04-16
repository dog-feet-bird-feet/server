package com.capstone.dfbf.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode{

    // 도메인 별 예외는 각 도메인 디렉토리에 BaseCode를 상속한 Code enum을 만들 것
    TMP_ERROR(HttpStatus.BAD_REQUEST, "TMP-0000", "임시 예외", ErrorDisplayType.MODAL),
    BINDING_ERROR(HttpStatus.BAD_REQUEST, "BINDING-0000", "바인딩 에러", ErrorDisplayType.POPUP),

    EMAIL_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "MEMBER-0002", "이메일이 존재합니다.", ErrorDisplayType.POPUP),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN-0001", "토큰ㅇ", ErrorDisplayType.POPUP),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-0000", "토큰 오류", ErrorDisplayType.POPUP),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.NOT_FOUND, "MEMBER-0001", "유효하지 않는 이메일, 비번", ErrorDisplayType.POPUP),
    EMPTY_TOKEN_PROVIDED(HttpStatus.UNAUTHORIZED, "TOKEN-0002", "토큰 텅텅", ErrorDisplayType.POPUP),
    FAIL_PROCEED(HttpStatus.FORBIDDEN, "FAIL", "", ErrorDisplayType.POPUP),
    PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "LOGIN-0000", "잘못된 비밀번호입니다.", ErrorDisplayType.POPUP),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-0000", "회원이 없습니다.", ErrorDisplayType.POPUP),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
