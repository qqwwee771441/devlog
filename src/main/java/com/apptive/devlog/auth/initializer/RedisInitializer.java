package com.apptive.devlog.auth.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisInitializer implements ApplicationRunner {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(ApplicationArguments args) {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}
