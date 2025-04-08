package com.apptive.devlog.domain.oauth2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OAuth2CallbackRequestDto {
    @NotBlank
    private String code;
    @NotBlank
    private String state;
}
