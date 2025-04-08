package com.apptive.devlog.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRefreshRequestDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
