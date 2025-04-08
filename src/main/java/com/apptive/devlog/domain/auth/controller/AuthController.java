package com.apptive.devlog.domain.auth.controller;

import com.apptive.devlog.domain.auth.dto.*;
import com.apptive.devlog.global.annotation.InjectToken;
import com.apptive.devlog.domain.auth.service.AuthService;
import com.apptive.devlog.global.response.api.ApiResponse;
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
    public ResponseEntity<ApiResponse<UserSignupResponseDto>> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto responseDto = authService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<UserRefreshResponseDto>> refresh(@Valid @InjectToken UserRefreshRequestDto requestDto) {
        UserRefreshResponseDto responseDto = authService.refresh(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @InjectToken UserLogoutRequestDto requestDto) {
        authService.logout(requestDto);
        return ResponseEntity.ok(ApiResponse.success("logout success", null));
    }
}
