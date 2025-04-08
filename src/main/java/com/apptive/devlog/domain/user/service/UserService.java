package com.apptive.devlog.domain.user.service;

import com.apptive.devlog.domain.user.dto.UserProfileRequestDto;
import com.apptive.devlog.domain.user.dto.UserProfileResponseDto;
import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.repository.UserRepository;
import com.apptive.devlog.global.response.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserProfileResponseDto getUserProfile(UserProfileRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);
        return new UserProfileResponseDto(user);
    }
}
