package io.eagle.wealthmarblebackend.domain.ContestParticipation.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ContestParticipationRepositoryImpl implements ContestParticipationRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Optional<Integer> getCurrentContestNum(Long cahootsId) {
        QContestParticipation cp = QContestParticipation.contestParticipation;
        return Optional.ofNullable(queryFactory
                    .select(cp.stocks.sum())
                    .from(cp)
                    .where(cp.vacation.id.eq(cahootsId))
                    .fetchOne());
    }
}
