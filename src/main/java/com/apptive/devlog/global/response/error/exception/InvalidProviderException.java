package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class InvalidProviderException extends CustomException {
    public InvalidProviderException() {
        super(ErrorCode.INVALID_PROVIDER);
    }
}
