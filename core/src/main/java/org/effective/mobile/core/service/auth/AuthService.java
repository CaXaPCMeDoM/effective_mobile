package org.effective.mobile.core.service.auth;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.effective.mobile.core.domain.JwtAuthentication;
import org.effective.mobile.core.entity.jwt.JwtRequest;
import org.effective.mobile.core.entity.jwt.JwtResponse;
import org.effective.mobile.core.entity.User;
import org.effective.mobile.core.exception.BadAuthorizationDataException;
import org.effective.mobile.core.exception.UserNotFoundException;
import org.effective.mobile.core.service.UserService;
import org.effective.mobile.core.service.jwt.RefreshTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public JwtResponse login(@NonNull JwtRequest request) throws BadAuthorizationDataException, UserNotFoundException {
        final User user = userService.getByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Ошибка авторизации"));
        if (user.getPassword().equals(request.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshTokenService.saveRefreshToken(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new BadAuthorizationDataException("Wrong password");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws UserNotFoundException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshTokenService.getRefreshToken(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, refreshToken);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws UserNotFoundException, AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshTokenService.getRefreshToken(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokenService.saveRefreshToken(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
