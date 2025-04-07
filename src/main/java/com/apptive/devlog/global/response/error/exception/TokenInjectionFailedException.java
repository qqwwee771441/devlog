package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class TokenInjectionFailedException extends CustomException {
    public TokenInjectionFailedException(String fieldName, Throwable cause) {
        super(ErrorCode.TOKEN_INJECTION_FAILED, cause);
    }
}
