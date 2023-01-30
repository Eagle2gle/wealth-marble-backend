package io.eagle.wealthmarblebackend.domain.user.repository;

import io.eagle.wealthmarblebackend.domain.user.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserById(Long userId);
}
