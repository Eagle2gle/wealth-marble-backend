package io.eagle.wealthmarblebackend.domain.cahoots.domain.ContestParticipation;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.ContestParticipation.QContestParticipation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ContestParticipationRepositoryImpl implements ContestParticipationRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public List<Integer> getCurrentContestNum(Long cahootsId) {
        QContestParticipation cp = QContestParticipation.contestParticipation;
        return queryFactory
                .select(cp.amount.sum())
                .from(cp)
                .where(cp.vacation.id.eq(cahootsId))
                .fetch();
    }
}
