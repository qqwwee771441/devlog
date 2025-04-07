package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
