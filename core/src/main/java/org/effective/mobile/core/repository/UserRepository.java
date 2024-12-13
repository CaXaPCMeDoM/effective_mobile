package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}
