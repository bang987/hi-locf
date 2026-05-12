package com.hi.locf.common.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
    LOCF_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "LOCF-404-RESULT", "LOCF 결과를 찾을 수 없습니다."),
    PROVISION_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRV-404-RESULT", "충당금 결과를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500", "시스템 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
