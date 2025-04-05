package com.apptive.devlog.auth.service;

import com.apptive.devlog.auth.utils.JwtTokenProvider;
import com.apptive.devlog.auth.dto.UserLoginRequestDto;
import com.apptive.devlog.auth.dto.UserLoginResponseDto;
import com.apptive.devlog.auth.dto.UserSignupResponseDto;
import com.apptive.devlog.auth.dto.UserSignupRequestDto;
import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.enums.Role;
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
}
