package com.apptive.devlog.auth.controller;

import com.apptive.devlog.auth.annotations.inject.token.InjectToken;
import com.apptive.devlog.auth.dto.*;
import com.apptive.devlog.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto response = authService.signup(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserRefreshResponseDto> refresh(@Valid @InjectToken UserRefreshRequestDto requestDto) {
        UserRefreshResponseDto responseDto = authService.refresh(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @InjectToken UserLogoutRequestDto requestDto) {
        authService.logout(requestDto);
        return ResponseEntity.ok().build();
    }
}
