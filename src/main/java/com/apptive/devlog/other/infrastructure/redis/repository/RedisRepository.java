package com.apptive.devlog.other.infrastructure.redis.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(10);

    public RedisRepository(
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            RedisTemplate<String, String> redisTemplate) {
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisTemplate = redisTemplate;
    }

    public void saveAccessToken(String accessToken, String email) {
        redisTemplate.opsForValue().set("AT:" + accessToken, email, accessTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public boolean hasAccessToken(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("AT:" + accessToken));
    }

    public void deleteAccessToken(String accessToken) {
        redisTemplate.delete("AT:" + accessToken);
    }

    public void saveRefreshToken(String refreshToken, String email) {
        redisTemplate.opsForValue().set("RT:" + refreshToken, email, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public boolean hasRefreshToken(String refreshToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("RT:" + refreshToken));
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete("RT:" + refreshToken);
    }
}
