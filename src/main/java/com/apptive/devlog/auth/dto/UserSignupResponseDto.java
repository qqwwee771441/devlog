package com.apptive.devlog.auth.dto;

import com.apptive.devlog.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSignupResponseDto {
    private final String email;

    public UserSignupResponseDto(User user) {
        this.email = user.getEmail();
    }
}
