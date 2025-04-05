package com.apptive.devlog.auth.service;

import com.apptive.devlog.auth.annotations.login.user.LoginUser;
import com.apptive.devlog.auth.annotations.login.user.UserInfo;
import com.apptive.devlog.auth.dto.*;
import com.apptive.devlog.auth.utils.JwtTokenProvider;
import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.enums.Role;
import com.apptive.devlog.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        User user = requestDto.toEntity(passwordEncoder);
        userRepository.save(user);
        return new UserSignupResponseDto(user);
    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), Role.USER);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail(), Role.USER);
        return new UserLoginResponseDto(accessToken, refreshToken);
    }

    public UserRefreshResponseDto refresh(UserRefreshRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        String storedRefreshToken = redisTemplate.opsForValue().get("RT:" + email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(email, Role.USER);
        return new UserRefreshResponseDto(newAccessToken);
    }

    public void logout(@LoginUser UserInfo userInfo) {
        redisTemplate.delete("RT:" + userInfo.getEmail());
    }
}
