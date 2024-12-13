package org.effective.mobile.core.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepositoryRedis implements RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepositoryRedis(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String email, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set(email, refreshToken, expirationTime, TimeUnit.SECONDS);
    }

    public String findByEmail(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void delete(String email) {
        redisTemplate.delete(email);
    }
}
