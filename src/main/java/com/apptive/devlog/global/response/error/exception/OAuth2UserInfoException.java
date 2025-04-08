package com.apptive.devlog.global.response.error.exception;

import com.apptive.devlog.global.response.error.model.ErrorCode;

public class OAuth2UserInfoException extends CustomException {
    public OAuth2UserInfoException(String message) {
        super(ErrorCode.OAUTH2_USERINFO_FAILED, message);
    }
}