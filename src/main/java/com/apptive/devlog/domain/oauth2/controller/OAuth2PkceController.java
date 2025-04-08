package com.apptive.devlog.domain.oauth2.controller;

import com.apptive.devlog.domain.oauth2.dto.OAuth2CallbackRequestDto;
import com.apptive.devlog.domain.oauth2.dto.OAuth2CallbackResponseDto;
import com.apptive.devlog.domain.oauth2.service.OAuth2PkceService;
import com.apptive.devlog.global.response.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuth2PkceController {

    private final OAuth2PkceService pkceService;

    @PostMapping("/pkce/callback")
    public ResponseEntity<ApiResponse<OAuth2CallbackResponseDto>> handleCallback(@Valid @RequestBody OAuth2CallbackRequestDto requestDto) {
        OAuth2CallbackResponseDto responseDto = pkceService.handleCallback(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}
