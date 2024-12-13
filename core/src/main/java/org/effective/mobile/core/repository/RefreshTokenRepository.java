package org.effective.mobile.core.repository;

public interface RefreshTokenRepository {
    void save(String email, String refreshToken, long expirationTime);

    String findByEmail(String email);

    void delete(String email);
}
