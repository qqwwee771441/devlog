package com.apptive.devlog.domain.user.dto;

import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.enums.Gender;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserProfileResponseDto {
    private String email;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private Gender gender;

    public UserProfileResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.birthday = user.getBirth();
        this.gender = user.getGender();
    }
}
