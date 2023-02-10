package io.eagle.repository;

import io.eagle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByNickname(String nickname);
    Optional<User> findUserByProviderId(String providerId);
}
