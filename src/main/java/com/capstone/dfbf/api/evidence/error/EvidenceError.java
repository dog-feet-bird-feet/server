package com.capstone.dfbf.api.evidence.error;

import com.capstone.dfbf.global.exception.error.BaseCode;
import com.capstone.dfbf.global.exception.error.ErrorDisplayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EvidenceError implements BaseCode {

    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "E000", "파일 크기는 10MB 이하여야 합니다.", ErrorDisplayType.POPUP),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "E001", "지원하지 않는 이미지 형식입니다. (jpg, png만 허용)", ErrorDisplayType.POPUP),
    INVALID_FILE_COUNT(HttpStatus.BAD_REQUEST, "E002", "대조물은 1~5장을 업로드 해야 합니다.", ErrorDisplayType.POPUP),;
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
