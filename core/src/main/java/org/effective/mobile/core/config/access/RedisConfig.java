package org.effective.mobile.core.config.access;

import org.effective.mobile.core.repository.RefreshTokenRepository;
import org.effective.mobile.core.repository.RefreshTokenRepositoryRedis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "cache", havingValue = "redis")
public class RedisConfig {
    @Bean
    public RefreshTokenRepository refreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        return new RefreshTokenRepositoryRedis(redisTemplate);
    }
}
