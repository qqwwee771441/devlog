package com.apptive.devlog.auth.dto;

import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.enums.Gender;
import com.apptive.devlog.domain.user.enums.Provider;
import com.apptive.devlog.domain.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotNull
    private LocalDate birth;

    @NotNull
    private Gender gender;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    private Provider provider;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .password(passwordEncoder.encode(password))
                .providers(Collections.singleton(provider))
                .role(Role.USER)
                .build();
    }
}
