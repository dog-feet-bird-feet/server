package com.capstone.dfbf.api.personality.error;

import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.exception.error.BaseCode;

public class PersonalityException extends BaseException {
    public PersonalityException(BaseCode code) {
        super(code);
    }
}
