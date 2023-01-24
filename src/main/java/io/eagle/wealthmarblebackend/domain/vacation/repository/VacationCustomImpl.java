package io.eagle.wealthmarblebackend.domain.vacation.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation;
import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.QVacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class VacationCustomImpl implements VacationCustom {

    private final JPQLQueryFactory queryFactory;

    @Value("${eagle.int.page}")
    private Integer page;

    @Value("${eagle.int.before_day}")
    private Integer beforeDays;

    private QVacation vacation = QVacation.vacation;
    private QContestParticipation cp = QContestParticipation.contestParticipation;

    public List<?> getVacationDetail(Long cahootsId){

        return queryFactory
                .select(vacation, cp.stocks.sum())
                .from(vacation)
                .leftJoin(vacation.historyList, cp)
                .where(vacation.id.eq(cahootsId))
                .fetchJoin()
                .distinct()
                .fetch();
    }

    public List<BreifCahootsDto> getVacationsBreif(VacationStatusType[] statusTypes, Integer offset){
        // TODO : picture 추가
        // TODO : no limit, offset 검토
        return queryFactory
                .select(Projections.fields(BreifCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.location,
                        vacation.status,
                        ExpressionUtils.as(vacation.stock.price,"stockPrice"),
                        ExpressionUtils.as(vacation.stock.num,"stockNum"),
                        ExpressionUtils.as(vacation.stockPeriod.start, "stockStart"),
                        ExpressionUtils.as(vacation.stockPeriod.end, "stockEnd"),
                        ExpressionUtils.as((cp.stocks.sum().coalesce(0).multiply(100).divide(vacation.stock.num)),"competitionRate")))
                .from(vacation)
                .leftJoin(vacation.historyList, cp)
                .where(isInStatus(statusTypes))
                .groupBy(vacation.id)
                .orderBy(cp.stocks.sum().desc())
                .limit(this.page)
                .offset(offset * this.page)
                .fetch();

    }

    public List<BreifV2CahootsDto> getVacationsBreifV2(VacationStatusType[] statusTypes, Integer offset){
        // TODO : picture 추가
        // TODO : no limit, offset 검토
        return queryFactory
                .select(Projections.fields(BreifCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.status,
                        ExpressionUtils.as(vacation.stockPeriod.start, "stockStart"),
                        ExpressionUtils.as(vacation.stockPeriod.end, "stockEnd")))
                .from(vacation)
                .where(isInStatus(statusTypes), isInDateRange())
                .orderBy(vacation.stockPeriod.end.asc())
                .limit(this.page)
                .offset(offset * this.page)
                .fetch();
    }

    private BooleanExpression isInStatus(VacationStatusType[] statusTypes){
        return statusTypes.length > 0 ? vacation.status.in(statusTypes) : null;
    }

    private BooleanExpression isInDateRange(){
        LocalDate today = LocalDate.now();
        return vacation.stockPeriod.end.between(today, today.plusDays(beforeDays));
    }
}
