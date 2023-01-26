package io.eagle.wealthmarblebackend.domain.ContestParticipation.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation.contestParticipation;

@RequiredArgsConstructor
public class ContestParticipationRepositoryImpl implements ContestParticipationRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Optional<Integer> getCurrentContestNum(Long cahootsId) {
        return Optional.ofNullable(queryFactory
                    .select(contestParticipation.stocks.sum())
                    .from(contestParticipation)
                    .where(contestParticipation.vacation.id.eq(cahootsId))
                    .fetchOne());
    }

    @Override
    public List<ContestParticipation> findAllByCahootsId(Long cahootsId){
        return queryFactory
                .selectFrom(contestParticipation)
                .where(contestParticipation.vacation.id.eq(cahootsId))
                .orderBy(contestParticipation.createdAt.desc())
                .fetch();

    }
}
