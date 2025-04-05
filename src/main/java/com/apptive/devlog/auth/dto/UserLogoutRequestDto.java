package com.apptive.devlog.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLogoutRequestDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
