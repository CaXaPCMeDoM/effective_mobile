package org.effective.mobile.core.entity.jwt;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {
    @Email
    private String email;
    @NotBlank
    private String password;
}
