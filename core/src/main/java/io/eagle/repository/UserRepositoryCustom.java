package io.eagle.repository;

import io.eagle.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserById(Long userId);
}
