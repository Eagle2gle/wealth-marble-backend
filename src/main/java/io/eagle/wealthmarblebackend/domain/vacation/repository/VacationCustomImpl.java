package io.eagle.wealthmarblebackend.domain.vacation.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation;
import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.QVacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RequiredArgsConstructor
public class VacationCustomImpl implements VacationCustom {

    private final JPQLQueryFactory queryFactory;

    @Value("${eagle.int.page}")
    private Integer page;용

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

    public List<BreifCahootsDto> getVacationsBreif(VacationStatusType status, Integer offset){
        QVacation vacation = QVacation.vacation;
        QContestParticipation cp = QContestParticipation.contestParticipation;
        // TODO : picture 추가
        // TODO : no limit, offset 검토
        return queryFactory
                .select(Projections.fields(BreifCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.location,
                        vacation.status,
                        ExpressionUtils.as(vacation.stock.price,"stockPrice"),
                        ExpressionUtils.as(vacation.stockPeriod.start, "stockStart"),
                        ExpressionUtils.as(vacation.stockPeriod.end, "stockEnd"),
                        ExpressionUtils.as(
                                (cp.stocks.sum().coalesce(0)
                                        .multiply(100)
                                        .divide(vacation.stock.num)),
                                "competitionRate"
                        )))
                .from(vacation)
                .leftJoin(vacation.historyList, cp)
                .where(vacation.status.eq(status))
                .groupBy(vacation.id)
                .orderBy(cp.stocks.sum().desc())
                .limit(this.page)
                .offset(offset * this.page)
                .fetch();

    }
}
