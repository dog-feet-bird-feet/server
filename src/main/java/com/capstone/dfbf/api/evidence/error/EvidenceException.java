package com.capstone.dfbf.api.evidence.error;

import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.exception.error.BaseCode;


public class EvidenceException extends BaseException {
    public EvidenceException(BaseCode baseCode) {
        super(baseCode);
    }
}
