package org.effective.mobile.core.entity.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER");

    private final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
