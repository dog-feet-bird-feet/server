package com.capstone.dfbf.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode{

    // 도메인 별 예외는 각 도메인 디렉토리에 BaseCode를 상속한 Code enum을 만들 것
    TMP_ERROR(HttpStatus.BAD_REQUEST, "TMP-0000", "임시 예외", ErrorDisplayType.MODAL),
    BINDING_ERROR(HttpStatus.BAD_REQUEST, "BINDING-0000", "바인딩 에러", ErrorDisplayType.POPUP);
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
