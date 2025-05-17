package com.capstone.dfbf.global.exception;

import com.capstone.dfbf.global.exception.error.BaseCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final BaseCode code;
    private String customErrorMessage;

    protected BaseException(BaseCode code) {
        super(code.getMessage());
        this.code = code;
    }

    protected BaseException(BaseCode code, final String message) {
        super(message);
        this.code = code;
        this.customErrorMessage = message;
    }

    public boolean hasCustomMessage(){
        return customErrorMessage != null;
    }

    public static BaseException from(BaseCode code) {
        return new BaseException(code);
    }

    public static BaseException from(BaseCode code, final String customErrorMessage){
        return new BaseException(code, customErrorMessage);
    }
}
