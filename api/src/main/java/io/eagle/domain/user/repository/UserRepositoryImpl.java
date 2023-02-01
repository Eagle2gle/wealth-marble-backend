package io.eagle.domain.user.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import static io.eagle.entity.QUser.user;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(jpqlQueryFactory
            .selectFrom(user)
            .where(user.id.eq(userId))
            .fetchOne());
    }

}
