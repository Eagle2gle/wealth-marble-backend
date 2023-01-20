package io.eagle.wealthmarblebackend.domain.user.repository;

import io.eagle.wealthmarblebackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
