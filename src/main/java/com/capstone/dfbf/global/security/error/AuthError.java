package com.capstone.dfbf.global.security.error;

import com.capstone.dfbf.global.exception.error.BaseCode;
import com.capstone.dfbf.global.exception.error.ErrorDisplayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthError implements BaseCode {
    UN_AUTHORIZED(HttpStatus.UNAUTHORIZED,"AUTH-000", "Unauthorized", ErrorDisplayType.POPUP),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
