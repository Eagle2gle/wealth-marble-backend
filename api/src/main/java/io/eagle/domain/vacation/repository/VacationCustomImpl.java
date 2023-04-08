package io.eagle.domain.vacation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.vacation.dto.*;
import io.eagle.domain.vacation.dto.response.*;
import io.eagle.domain.vacation.vo.DetailCahootsVO;
import io.eagle.domain.vacation.vo.MarketQueryVO;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;
import static io.eagle.entity.QContestParticipation.contestParticipation;
import static io.eagle.entity.QInterest.interest;
import static io.eagle.entity.QPicture.picture;
import static io.eagle.entity.QVacation.vacation;
import static io.eagle.entity.QPriceInfo.priceInfo;
import static io.eagle.entity.QTransaction.transaction;

@RequiredArgsConstructor
public class VacationCustomImpl implements VacationCustom {

    private final JPQLQueryFactory queryFactory;
    private final EntityManager entityManager;

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
                        vacation.country,
                        vacation.status,
                        vacation.theme.themeLocation,
                        vacation.theme.themeBuilding,
                        vacation.plan.expectedTotalCost,
                        vacation.plan.expectedMonth,
                        vacation.shortDescription,
                        vacation.description,
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

    public List<DetailCahootsVO> findVacationDetail(Long cahootsId, Long userId){
        JPQLQuery<DetailCahootsVO> query = queryFactory
                .select(Projections.fields(DetailCahootsVO.class,
                        vacation.id,
                        vacation.title,
                        vacation.location,
                        vacation.country,
                        vacation.status,
                        vacation.theme.themeLocation,
                        vacation.theme.themeBuilding,
                        vacation.plan.expectedTotalCost,
                        vacation.plan.expectedMonth,
                        vacation.shortDescription,
                        vacation.description,
                        vacation.expectedRateOfReturn,
                        vacation.stock.price.as("stockPrice"),
                        vacation.stock.num.as("stockNum"),
                        vacation.stockPeriod.start.as("stockStart"),
                        vacation.stockPeriod.end.as("stockEnd"),
                        picture.url.as("images"),
                        ExpressionUtils.as(JPAExpressions.select(interest.id.count().intValue()).from(interest)
                                        .where(interest.vacation.id.eq(cahootsId)),"interestCount"),
                        interest.user.id.isNotNull().as("isInterest"),
                        ExpressionUtils.as((contestParticipation.stocks.sum().coalesce(0).multiply(100).divide(vacation.stock.num)), "competitionRate")))
                .from(vacation)
                .leftJoin(vacation.historyList, contestParticipation)
                .leftJoin(picture).on(picture.vacation.id.eq(cahootsId))
                .leftJoin(interest);

        if(userId == null){
            return query.on(interest.vacation.id.eq(cahootsId))
                    .where(vacation.id.eq(cahootsId))
                    .groupBy(picture.url)
                    .fetch();
        }
        return query.on(interest.vacation.id.eq(cahootsId), isInterest(userId))
                .where(vacation.id.eq(cahootsId))
                .groupBy(picture.url)
                .fetch();
    }

    public List<Long> findVacationIdByUserInterested(Long userId) {
        return queryFactory.select(vacation.id).from(interest).innerJoin(interest.vacation, vacation).on(interest.user.id.eq(userId)).fetch();
    }

    public List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto){
        // TODO : no limit, offset 검토
        return queryFactory
                .select(Projections.fields(BreifCahootsDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.country.as("location"),
                        vacation.status,
                        vacation.stock.price.as("stockPrice"),
                        vacation.stock.num.as("stockNum"),
                        vacation.stockPeriod.start.as("stockStart"),
                        vacation.stockPeriod.end.as("stockEnd"),
                        ExpressionUtils.as((contestParticipation.stocks.sum().coalesce(0).multiply(100).divide(vacation.stock.num)),"competitionRate")
                ))
                .from(vacation)
                .leftJoin(vacation.historyList, contestParticipation)
                .where(isInStatus(infoConditionDto.getTypes()), hasKeyword(infoConditionDto.getKeyword()))
                .groupBy(vacation.id)
                .orderBy(contestParticipation.stocks.sum().desc())
                .limit(this.page)
                .offset(infoConditionDto.getPage() * this.page)
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
                .offset(infoConditionDto.getPage() * this.page)
                .fetch();
    }

    public List<ImminentInfoDto> findByImminentEndVacation(){
        return queryFactory.select(Projections.fields(ImminentInfoDto.class,
                        vacation.id,
                        vacation.title,
                        ExpressionUtils.as((contestParticipation.stocks.sum().coalesce(0).multiply(100).divide(vacation.stock.num)), "competitionRate")
                ))
                .from(vacation)
                .leftJoin(vacation.historyList, contestParticipation)
                .where(vacation.status.eq(VacationStatusType.CAHOOTS_ONGOING))
                .groupBy(vacation.id)
                .orderBy(vacation.stockPeriod.end.asc())
                .limit(this.page)
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

    @Override
    public List<Vacation> findAllMarkets(Pageable pageable, String keyword) {
        return queryFactory
            .selectFrom(vacation)
            .where(vacation.status.eq(VacationStatusType.MARKET_ONGOING), hasKeyword(keyword))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<String> getCountries(VacationStatusType type){
        return queryFactory
                .select(vacation.country)
                .from(vacation)
                .where(vacation.status.eq(type))
                .groupBy(vacation.country)
                .fetch();
    }

    @Override
    public MarketRankDto findMarketRankInfoById(Long id) {
        String sql = "" +
            "SELECT v.id as vacationId, " +
            "pic.url as pictureUrl, " +
            "v.title as title, " +
            "t.price as currentPrice, " +
            "price.standard_price as startPrice " +
            "FROM vacation AS v " +
            "LEFT JOIN price_info AS price ON price.id = (SELECT MAX(p2.id) FROM price_info AS p2 WHERE p2.vacation_id = ?) " +
            "LEFT JOIN picture AS pic ON pic.id = (SELECT MAX(pic2.id) FROM picture AS pic2 WHERE pic2.cahoots_id = ?) " +
            "LEFT JOIN transaction AS t ON t.id = (SELECT MAX(t2.id) FROM transaction AS t2 WHERE t2.vacation_id = ?) " +
            "WHERE v.id = ?";

        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, id);
        query.setParameter(2, id);
        query.setParameter(3, id);
        query.setParameter(4, id);

        List<MarketQueryVO> marketQueryVO = jpaResultMapper.list(query, MarketQueryVO.class);
        return marketQueryVO.size() == 0 ? null : new MarketRankDto(marketQueryVO.get(0));
    }

    public RecommendMarketDto getRecommendMarket(Long vacationId, Long userId){
        JPQLQuery<RecommendMarketDto> tempQuery = queryFactory
                .select(Projections.fields(RecommendMarketDto.class,
                        vacation.id,
                        vacation.title,
                        vacation.expectedRateOfReturn,
                        picture.url.as("image"),
                        interest.isNotNull().as("isInterest")
                ))
                .from(vacation)
                .leftJoin(picture).on(picture.vacation.id.eq(vacationId));

        if(userId == null){
            List<RecommendMarketDto> result = tempQuery
                    .leftJoin(interest).on(interest.vacation.id.eq(vacationId))
                    .where(vacation.id.eq(vacationId))
                    .fetch();
            result.get(0).setIsInterest(false);
            return result.get(0);
        }
        List<RecommendMarketDto> result = tempQuery
                .leftJoin(interest).on(interest.vacation.id.eq(vacationId), isInterest(userId))
                .where(vacation.id.eq(vacationId))
                .fetch();
        return result.get(0);
    }

    @Override
    public List<MarketRankDto> findTop5VacationByReward() {
        String sql = findTop5VacationSQL("t.price");
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        Query query = entityManager.createNativeQuery(sql);
        return jpaResultMapper.list(query, MarketQueryVO.class)
            .stream().map(MarketRankDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<MarketRankDto> findTop5VacationByTransaction() {
        String sql = findTop5VacationSQL("price.transaction_amount");
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        Query query = entityManager.createNativeQuery(sql);
        return jpaResultMapper.list(query, MarketQueryVO.class)
            .stream().map(MarketRankDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<MarketRankDto> findTop5MarketByPrice() {
        String sql = findTop5VacationSQL("t.price");
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        Query query = entityManager.createNativeQuery(sql);
        return jpaResultMapper.list(query, MarketQueryVO.class)
            .stream().map(MarketRankDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<MarketRankDto> findTop5MarketByPriceRate() {
        String sql = findTop5VacationSQL(
            "(t.price - price.standard_price) / price.standard_price * 100"
        );
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        Query query = entityManager.createNativeQuery(sql);
        return jpaResultMapper.list(query, MarketQueryVO.class)
            .stream().map(MarketRankDto::new)
            .collect(Collectors.toList());
    }

    private String findTop5VacationSQL(String property) {
        return "" +
            "SELECT v.id as vacationId, " +
            "pic.url as pictureUrl, " +
            "v.title as title, " +
            "t.price as currentPrice, " +
            "price.standard_price as startPrice " +
            "FROM vacation AS v " +
            "LEFT JOIN price_info AS price ON price.id = (SELECT MAX(p2.id) FROM price_info AS p2 WHERE p2.vacation_id = v.id) " +
            "LEFT JOIN picture AS pic ON pic.id = (SELECT MAX(pic2.id) FROM picture AS pic2 WHERE pic2.cahoots_id = v.id) " +
            "LEFT JOIN transaction AS t ON t.id = (SELECT MAX(t2.id) FROM transaction AS t2 WHERE t2.vacation_id = v.id) " +
            "ORDER BY " + property + " " +
            "LIMIT 5";
    }

    private BooleanExpression isInStatus(VacationStatusType[] statusTypes){
        return statusTypes.length > 0 ? vacation.status.in(statusTypes) : null;
    }

    private BooleanExpression hasKeyword(String keyword){
        return keyword != null && !keyword.isBlank() ? (
                vacation.title.contains(keyword)
                .or(vacation.location.contains(keyword))
                .or(vacation.theme.themeBuilding.stringValue().contains(keyword)
                .or(vacation.shortDescription.contains(keyword)))
        ) : null;
    }

    private BooleanExpression isInStartDateRange(Integer interval){
        LocalDate today = LocalDate.now();
        return vacation.stockPeriod.start.between(today.minusDays(interval), today);
    }

    private BooleanExpression isInEndDateRange(Integer interval){
        LocalDate today = LocalDate.now();
        return vacation.stockPeriod.end.between(today, today.plusDays(interval));
    }

    private BooleanExpression isInterest(Long userId){
        return userId != null ? interest.user.id.eq(userId) : null;
    }
}
