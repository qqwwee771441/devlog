package com.apptive.devlog.domain.user.controller;

import com.apptive.devlog.auth.annotations.inject.email.InjectEmail;
import com.apptive.devlog.domain.user.dto.UserProfileRequestDto;
import com.apptive.devlog.domain.user.dto.UserProfileResponseDto;
import com.apptive.devlog.domain.user.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@Valid @InjectEmail UserProfileRequestDto requestDto) {
        return ResponseEntity.ok(userService.getUserProfile(requestDto));
    }
}
