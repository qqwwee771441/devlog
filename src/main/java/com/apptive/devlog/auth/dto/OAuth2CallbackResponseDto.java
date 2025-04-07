package com.apptive.devlog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2CallbackResponseDto {
    private String accessToken;
    private String refreshToken;
}
