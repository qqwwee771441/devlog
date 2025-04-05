package com.apptive.devlog.domain.user.controller;

import com.apptive.devlog.auth.annotations.loginuser.LoginUser;
import com.apptive.devlog.auth.annotations.loginuser.UserInfo;
import com.apptive.devlog.domain.user.dto.UserProfileResponseDto;
import com.apptive.devlog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@LoginUser UserInfo userInfo) {
        return ResponseEntity.ok(userService.getUserProfile(userInfo));
    }
}
