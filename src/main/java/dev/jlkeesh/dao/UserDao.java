package dev.jlkeesh.dao;

import dev.jlkeesh.domain.User;
import lombok.NonNull;

import java.util.Optional;

public interface UserDao extends BaseDao<User, Long> {
    Optional<User> findByUsername(@NonNull String username);

    Optional<User> findByEmail(@NonNull String email);

    Optional<User> findByEmailOrUsername(@NonNull String subject);

    void resetPassword(Long userId, String newPassword);
}
