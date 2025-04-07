package com.apptive.devlog.auth.dto;

import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.enums.Gender;
import com.apptive.devlog.domain.user.enums.Provider;
import com.apptive.devlog.domain.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {
    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 20)
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    private String nickname;

    private LocalDate birth;

    private Gender gender;

    public User toEntity(PasswordEncoder passwordEncoder) {
        User user = User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        user.addProvider(Provider.LOCAL);
        return user;
    }
}
