package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class OAuth2TokenRequestException extends CustomException {
    public OAuth2TokenRequestException(String message) {
        super(ErrorCode.OAUTH2_TOKEN_REQUEST_FAILED, message);
    }
}