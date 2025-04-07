package com.apptive.devlog.domain.auth.service;

import com.apptive.devlog.domain.auth.dto.*;
import com.apptive.devlog.domain.user.enums.Provider;
import com.apptive.devlog.global.response.error.exception.DuplicateEmailException;
import com.apptive.devlog.global.response.error.exception.InvalidEmailOrPasswordException;
import com.apptive.devlog.global.response.error.exception.InvalidProviderException;
import com.apptive.devlog.global.response.error.exception.InvalidRefreshTokenException;
import com.apptive.devlog.global.security.jwt.JwtTokenProvider;
import com.apptive.devlog.other.infrastructure.redis.repository.RedisRepository;
import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisRepository;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateEmailException();
        }
        User user = requestDto.toEntity(passwordEncoder);
        userRepository.save(user);
        return new UserSignupResponseDto(user);
    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }
        if (!user.getProviders().contains(Provider.LOCAL)) {
            throw new InvalidProviderException();
        }
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
        return new UserLoginResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public UserRefreshResponseDto refresh(UserRefreshRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        redisRepository.deleteAccessToken(accessToken);
        redisRepository.deleteRefreshToken(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new UserRefreshResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(UserLogoutRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();

        redisRepository.deleteAccessToken(accessToken);
        redisRepository.deleteRefreshToken(refreshToken);
    }
}
