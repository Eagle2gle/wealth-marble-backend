package io.eagle.domain.interest.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static io.eagle.entity.QInterest.*;

@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<Interest> findAllByUser(User user) {
        return jpqlQueryFactory
            .selectFrom(interest)
            .leftJoin(interest.user)
            .where(interest.user.id.eq(user.getId()))
            .fetchAll()
            .stream()
            .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByUser(User user) {
        return jpqlQueryFactory
            .selectOne()
            .from(interest)
            .where(interest.user.id.eq(user.getId()))
            .fetchFirst() != null;
    }

    @Override
    public Interest findByUserAndVacation(Long userId, Long vacationId) {
        return jpqlQueryFactory
            .selectFrom(interest)
            .where(interest.user.id.eq(userId).and(interest.vacation.id.eq(vacationId)))
            .fetchOne();
    }


}
