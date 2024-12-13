package org.effective.mobile.core.service.jwt;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.effective.mobile.core.domain.JwtAuthentication;
import org.effective.mobile.core.entity.enums.Role;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setEmail(claims.get("email", String.class));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final String roles = claims.get("roles", String.class);
        return Arrays.stream(roles.split(","))
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
