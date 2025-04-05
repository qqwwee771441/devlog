package com.apptive.devlog.auth.service;

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

    @Transactional
    public UserRefreshResponseDto refresh(UserRefreshRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = redisTemplate.opsForValue().get("RT:" + refreshToken);
        if (email == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        redisTemplate.delete("AT:" + accessToken);
        redisTemplate.delete("RT:" + refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(email, Role.USER);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email, Role.USER);

        return new UserRefreshResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(UserLogoutRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();

        redisTemplate.delete("AT:" + accessToken);
        redisTemplate.delete("RT:" + refreshToken);
    }
}
