package com.capstone.dfbf.api.member.error;

import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.exception.error.BaseCode;

public class MemberException extends BaseException {
    public MemberException(BaseCode baseCode) {
        super(baseCode);
    }
}
