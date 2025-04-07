package com.apptive.devlog.domain.oauth2.service;

import com.apptive.devlog.domain.oauth2.dto.OAuth2CallbackRequestDto;
import com.apptive.devlog.domain.oauth2.dto.OAuth2CallbackResponseDto;
import com.apptive.devlog.global.response.error.exception.InvalidStateFormatException;
import com.apptive.devlog.global.response.error.exception.OAuth2TokenRequestException;
import com.apptive.devlog.global.response.error.exception.OAuth2UserInfoException;
import com.apptive.devlog.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2PkceService {

    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public OAuth2CallbackResponseDto handleCallback(OAuth2CallbackRequestDto requestDto) {
        String code = requestDto.getCode();
        String state = requestDto.getState();

        String[] parts = state != null ? state.split("::") : new String[0];
        if (parts.length != 2) {
            throw new InvalidStateFormatException();
        }
        String deviceId = parts[0];
        String codeVerifier = parts[1];

        Map<String, Object> tokenResponse = requestAccessToken(code, codeVerifier);
        String googleAccessToken = Optional.ofNullable(tokenResponse.get("access_token"))
                .map(Object::toString)
                .orElseThrow(() -> new OAuth2TokenRequestException("Access token not found"));

        Map<String, Object> userInfo = fetchGoogleUserInfo(googleAccessToken);
        String email = Optional.ofNullable(userInfo.get("email"))
                .map(Object::toString)
                .orElseThrow(() -> new OAuth2UserInfoException("Email not found in user info"));

        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new OAuth2CallbackResponseDto(accessToken, refreshToken);
    }

    private Map<String, Object> requestAccessToken(String code, String codeVerifier) {
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("grant_type", "authorization_code");
        tokenRequest.add("code", code);
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("code_verifier", codeVerifier);

        try {
            return webClient.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .bodyValue(tokenRequest)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            throw new OAuth2TokenRequestException("Failed to get token from Google: " + ex.getResponseBodyAsString());
        }
    }

    private Map<String, Object> fetchGoogleUserInfo(String accessToken) {
        try {
            return webClient.get()
                    .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            throw new OAuth2UserInfoException("Failed to get user info from Google: " + ex.getResponseBodyAsString());
        }
    }
}
