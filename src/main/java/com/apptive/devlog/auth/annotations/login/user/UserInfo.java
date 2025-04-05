package com.apptive.devlog.auth.annotations.login.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserInfo implements Serializable {
    private String email;
}
