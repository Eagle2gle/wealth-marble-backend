package io.eagle.wealthmarblebackend.domain.vacation.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.QVacation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VacationCustomImpl implements VacationCustom {

    private final JPQLQueryFactory queryFactory;

    public List<?> getVacationDetail(Long cahootsId){
        QVacation vacation = QVacation.vacation;
        QContestParticipation cp = QContestParticipation.contestParticipation;
        return queryFactory
                .select(vacation, cp.stocks.sum())
                .from(vacation)
                .leftJoin(vacation.historyList, cp)
                .where(vacation.id.eq(cahootsId))
                .fetchJoin()
                .distinct()
                .fetch();
    }
}
