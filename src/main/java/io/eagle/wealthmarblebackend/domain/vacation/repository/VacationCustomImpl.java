package io.eagle.wealthmarblebackend.domain.vacation.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.vacation.dto.*;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.QContestParticipation.*;
import static io.eagle.wealthmarblebackend.domain.picture.entity.QPicture.*;
import static io.eagle.wealthmarblebackend.domain.vacation.entity.QVacation.*;

@RequiredArgsConstructor
public class VacationCustomImpl implements VacationCustom {

    private final JPQLQueryFactory queryFactory;

    @Value("${eagle.int.page}")
    private Integer page;

    @Value("${eagle.int.before_day}")
    private Integer beforeDays;

    @Value("${eagle.int.recent_day}")
    private Integer recentDays;


    public DetailCahootsDto getVacationDetail(Long cahootsId){
        return queryFactory
                .select(Projections.fields(DetailCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.location,
                        vacation.status,
                        vacation.theme.themeLocation,
                        vacation.theme.themeBuilding,
                        vacation.plan.expectedTotalCost,
                        vacation.plan.expectedMonth,
                        vacation.shortDescription,
                        vacation.descritption,
                        vacation.expectedRateOfReturn,
                        vacation.stock.price.as("stockPrice"),
                        vacation.stock.num.as("stockNum"),
                        vacation.stockPeriod.start.as("stockStart"),
                        vacation.stockPeriod.end.as("stockEnd"),
                        ExpressionUtils.as((contestParticipation.stocks.sum().coalesce(0).multiply(100).divide(vacation.stock.num)), "competitionRate")))
                .from(vacation)
                .leftJoin(vacation.historyList, contestParticipation)
                .where(vacation.id.eq(cahootsId))
                .fetchOne();
    }

    public List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto){
        // TODO : no limit, offset 검토
        return queryFactory
                .select(Projections.fields(BreifCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.location,
                        vacation.status,
                        vacation.stock.price.as("stockPrice"),
                        vacation.stock.num.as("stockNum"),
                        vacation.stockPeriod.start.as("stockStart"),
                        vacation.stockPeriod.end.as("stockEnd"),
                        ExpressionUtils.as((contestParticipation.stocks.sum().coalesce(0).multiply(100).divide(vacation.stock.num)),"competitionRate")))
                .from(vacation)
                .leftJoin(vacation.historyList, contestParticipation)
                .where(isInStatus(infoConditionDto.getTypes()), hasKeyword(infoConditionDto.getKeyword()))
                .groupBy(vacation.id)
                .orderBy(contestParticipation.stocks.sum().desc())
                .limit(this.page)
                .offset(infoConditionDto.getOffset() * this.page)
                .fetch();

    }

    public List<BreifV2CahootsDto> getVacationsBreifV2(InfoConditionDto infoConditionDto){
        // TODO : no limit, offset 검토
        return queryFactory
                .select(Projections.fields(BreifV2CahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.status,
                        vacation.stockPeriod.start.as("stockStart"),
                        vacation.stockPeriod.end.as("stockEnd")))
                .from(vacation)
                .where(isInStatus(infoConditionDto.getTypes()), isInEndDateRange(beforeDays))
                .orderBy(vacation.stockPeriod.end.asc())
                .limit(this.page)
                .offset(infoConditionDto.getOffset() * this.page)
                .fetch();
    }

    public List<LatestCahootsDto> findLatestVacations(){
        return queryFactory
                .select(Projections.fields(LatestCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.stockPeriod.start.as("stockStart"),
                        vacation.expectedRateOfReturn))
                .from(vacation)
                .where(isInStatus(new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING}), isInStartDateRange(recentDays))
                .orderBy(vacation.stockPeriod.start.desc())
                .limit(this.page)
                .fetch();
    }

    private BooleanExpression isInStatus(VacationStatusType[] statusTypes){
        return statusTypes.length > 0 ? vacation.status.in(statusTypes) : null;
    }

    private BooleanExpression hasKeyword(String keyword){
        // TODO : 검색 범위 논의
        return keyword != null && !keyword.isBlank() ? (vacation.title.contains(keyword).or(vacation.location.contains(keyword))) : null;
    }

    private BooleanExpression isInStartDateRange(Integer interval){
        LocalDate today = LocalDate.now();
        return vacation.stockPeriod.start.between(today.minusDays(interval), today);
    }

    private BooleanExpression isInEndDateRange(Integer interval){
        LocalDate today = LocalDate.now();
        return vacation.stockPeriod.end.between(today, today.plusDays(interval));
    }
}
