package com.apptive.devlog.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotNull
    private String password;
}
