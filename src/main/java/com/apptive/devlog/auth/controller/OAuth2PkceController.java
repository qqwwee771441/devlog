package com.apptive.devlog.auth.controller;

import com.apptive.devlog.auth.dto.OAuth2CallbackRequestDto;
import com.apptive.devlog.auth.dto.OAuth2CallbackResponseDto;
import com.apptive.devlog.auth.service.OAuth2PkceService;
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
    public ResponseEntity<OAuth2CallbackResponseDto> handleCallback(@Valid @RequestBody OAuth2CallbackRequestDto requestDto) {
        OAuth2CallbackResponseDto responseDto = pkceService.handleCallback(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
