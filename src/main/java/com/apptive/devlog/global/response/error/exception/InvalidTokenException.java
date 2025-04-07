package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
