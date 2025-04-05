package com.apptive.devlog.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileRequestDto {
    @NotBlank
    String email;
}
