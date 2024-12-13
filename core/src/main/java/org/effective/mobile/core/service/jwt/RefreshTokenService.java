package org.effective.mobile.core.service.jwt;

import org.effective.mobile.core.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpirationDays;

    public RefreshTokenService(
            @Value("${jwt.refresh.life_time.refresh_token}") String refreshTokenExpirationDays,
            RefreshTokenRepository refreshTokenRepository
    )    {
        this.refreshTokenExpirationDays = DurationStyle.detectAndParse(refreshTokenExpirationDays).toSeconds();
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(String email, String refreshToken) {
        Duration duration = Duration.ofDays(refreshTokenExpirationDays);
        long expirationTime = duration.toSeconds();
        refreshTokenRepository.save(email, refreshToken, expirationTime);
    }

    public String getRefreshToken(String email) {
        return refreshTokenRepository.findByEmail(email);
    }

    public void deleteRefreshToken(String email) {
        refreshTokenRepository.delete(email);
    }
}
