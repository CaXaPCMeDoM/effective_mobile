package org.effective.mobile.core.entity;

import lombok.*;
import org.effective.mobile.core.entity.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private Role role;
}
