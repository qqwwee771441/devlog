package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class UnauthenticatedUserException extends CustomException {
    public UnauthenticatedUserException() {
        super(ErrorCode.UNAUTHENTICATED_USER);
    }
}