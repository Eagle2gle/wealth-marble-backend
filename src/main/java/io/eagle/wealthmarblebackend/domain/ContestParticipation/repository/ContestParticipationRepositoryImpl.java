package io.eagle.wealthmarblebackend.domain.ContestParticipation.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation;
import lombok.RequiredArgsConstructor;

import java.util.List;
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

    @Override
    public List<ContestParticipation> findAllByCahootsId(Long cahootsId){
        QContestParticipation cp = QContestParticipation.contestParticipation;
        return queryFactory
                .selectFrom(cp)
                .where(cp.vacation.id.eq(cahootsId))
                .orderBy(cp.createdAt.desc())
                .fetch();

    }
}
