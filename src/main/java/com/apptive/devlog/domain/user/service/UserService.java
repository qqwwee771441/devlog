package com.apptive.devlog.domain.user.service;

import com.apptive.devlog.auth.annotations.login.user.UserInfo;
import com.apptive.devlog.domain.user.dto.UserProfileResponseDto;
import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserProfileResponseDto getUserProfile(UserInfo userInfo) {
        Optional<User> user = userRepository.findByEmail(userInfo.getEmail());
        return new UserProfileResponseDto(user.orElseThrow());
    }
}
