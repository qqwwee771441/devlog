package com.apptive.devlog.auth.utils;

import com.apptive.devlog.auth.repository.RedisRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RedisRepository redisRepository;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            RedisRepository redisRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisRepository = redisRepository;
    }

    public String generateToken(String email, long expiration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(java.util.Date.from(java.time.Instant.now()))
                .setExpiration(java.util.Date.from(java.time.Instant.now().plusMillis(expiration)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String email) {
        String accessToken = generateToken(email, accessTokenExpiration);
        redisRepository.saveAccessToken(accessToken, email);
        return accessToken;
    }

    public String generateRefreshToken(String email) {
        String refreshToken = generateToken(email, refreshTokenExpiration);
        redisRepository.saveRefreshToken(refreshToken, email);
        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String token) {
        if (!validateToken(token)) return false;
        return redisRepository.hasAccessToken(token);
    }

    public boolean validateRefreshToken(String token) {
        if (!validateToken(token)) return false;
        return redisRepository.hasRefreshToken(token);
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
