package io.eagle.repository;

import io.eagle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByNickname(String nickname);
    Optional<User> findUserByProviderId(String providerId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.cash = ?2 WHERE u.id = ?1")
    int updateCash(Long id, Long cash);
}
