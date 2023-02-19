package io.eagle.domain.interest.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static io.eagle.entity.QInterest.*;

@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<Interest> findInterestByUser(User user, Pageable pageable) {
        return jpqlQueryFactory
            .selectFrom(interest)
            .leftJoin(interest.user)
            .where(interest.user.id.eq(user.getId()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", interest.createdAt, "%Y-%m-%d").desc())
            .fetch();
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
            .fetchFirst();
    }

    @Override
    public List<Interest> findAllByVacation(Long vacationId) {
        return jpqlQueryFactory
            .selectFrom(interest)
            .where(interest.vacation.id.eq(vacationId))
            .fetch();
    }

}
