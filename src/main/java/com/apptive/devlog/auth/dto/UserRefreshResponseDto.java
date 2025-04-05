package com.apptive.devlog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRefreshResponseDto {
    private String accessToken;
}
