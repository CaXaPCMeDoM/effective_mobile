package org.effective.mobile.core.service;

import lombok.RequiredArgsConstructor;
import org.effective.mobile.core.entity.User;
import org.effective.mobile.core.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Возвращает пользователя по email.
     * @param email email пользователя
     * @return найденный пользователь
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
