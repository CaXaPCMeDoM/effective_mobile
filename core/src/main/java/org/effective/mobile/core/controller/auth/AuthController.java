package org.effective.mobile.core.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.effective.mobile.core.entity.jwt.JwtRequest;
import org.effective.mobile.core.entity.jwt.JwtResponse;
import org.effective.mobile.core.exception.BadAuthorizationDataException;
import org.effective.mobile.core.exception.UserNotFoundException;
import org.effective.mobile.core.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Аутентификация с JWT")
public class AuthController {
    private static final String AUTHORIZATION = "Authorization";
    private static final String REFRESH_TOKEN = "refresh_token";
    private final long jwtRefreshSeconds;
    private final long jwtGenerateRefreshTokenDay;
    private final AuthService authService;

    public AuthController(
            @Value("${jwt.refresh.life_time.access_token}") String jwtRefreshSeconds,
            @Value("${jwt.refresh.life_time.refresh_token}") String jwtGenerateRefreshTokenDay,
            AuthService authService
    ) {
        this.jwtRefreshSeconds = DurationStyle.detectAndParse(jwtRefreshSeconds).getSeconds();
        this.jwtGenerateRefreshTokenDay = DurationStyle.detectAndParse(jwtGenerateRefreshTokenDay).getSeconds();
        this.authService = authService;
    }

    @Operation(summary = "Аутентификация пользователя",
            description = """
                    Авторизация с помощью jwt токенов.
                    Кладет в cookie refresh и access токены.
                    Время их жизни определяются конфигурацией приложения.
                    """)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Аутентификация успешна",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest authRequest,
            HttpServletResponse response)
            throws UserNotFoundException, BadAuthorizationDataException {
        final JwtResponse token;
        token = authService.login(authRequest);
        addTokenToCookie(response, token.getAccessToken(), AUTHORIZATION, (int) jwtRefreshSeconds);
        addTokenToCookie(response, token.getRefreshToken(), REFRESH_TOKEN, (int) jwtGenerateRefreshTokenDay);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Получить новый Access Token",
            description = """
                    Обновляет Access Token по Refresh Token из cookies.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новый токен сгенерирован", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(
            @CookieValue(REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response)
            throws UserNotFoundException {
        final JwtResponse token;
        token = authService.getAccessToken(refreshToken);

        addTokenToCookie(response, token.getAccessToken(), AUTHORIZATION, (int) jwtRefreshSeconds);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Обновить Refresh Token", description = """
            Обновляет Refresh Token по старому из cookies.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новый токен сгенерирован", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "401", description = "Ошибка авторизации")
    })
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(
            @CookieValue(REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response)
            throws UserNotFoundException, AuthException {
        final JwtResponse token;
        token = authService.refresh(refreshToken);
        addTokenToCookie(response, token.getRefreshToken(), REFRESH_TOKEN, (int) jwtGenerateRefreshTokenDay);
        return ResponseEntity.ok(token);
    }

    private void addTokenToCookie(
            HttpServletResponse response,
            String token, String cookieName,
            int lifeTime) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setPath("/");
        cookie.setMaxAge(lifeTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
