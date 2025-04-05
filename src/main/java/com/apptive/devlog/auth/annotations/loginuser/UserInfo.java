package com.apptive.devlog.auth.annotations.loginuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserInfo implements Serializable {
    private String email;
}
