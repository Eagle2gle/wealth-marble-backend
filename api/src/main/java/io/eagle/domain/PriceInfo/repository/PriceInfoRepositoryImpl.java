package io.eagle.domain.PriceInfo.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import io.eagle.domain.PriceInfo.dto.QChartResponseDto;
import io.eagle.entity.PriceInfo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static io.eagle.entity.QPriceInfo.*;

@RequiredArgsConstructor
public class PriceInfoRepositoryImpl implements PriceInfoRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<ChartResponseDto> findAllByVacationOrderByCreatedAt(Long vacationId, ChartRequestDto chartRequestDto) {
        DateTemplate<LocalDateTime> formattedDate = Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", priceInfo.createdAt, "%Y-%m-%d");
        DateTemplate<LocalDateTime> beforeDate = Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", chartRequestDto.getStartDate(), "%Y-%m-%d");
        DateTemplate<LocalDateTime> afterDate = Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", chartRequestDto.getEndDate(), "%Y-%m-%d");
        return jpqlQueryFactory
            .select(new QChartResponseDto(priceInfo.createdAt, priceInfo.standardPrice, priceInfo.transactionAmount))
            .from(priceInfo)
            .where(priceInfo.vacation.id.eq(vacationId).and(formattedDate.between(beforeDate, afterDate)))
            .orderBy(new OrderSpecifier(
                Order.DESC,
                priceInfo.createdAt
            ))
            .fetch();
    }

    @Override
    public PriceInfo getYesterdayPriceInfo() {
        DateTemplate<LocalDateTime> formattedDate = Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", priceInfo.createdAt, "%Y-%m-%d");
        DateTemplate<LocalDateTime> yesterday = Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", LocalDate.now().minusDays(1), "%Y-%m-%d");
        List<PriceInfo> priceInfos = jpqlQueryFactory
            .selectFrom(priceInfo)
            .where(formattedDate.eq(yesterday))
            .fetch();

        if (priceInfos == null || priceInfos.size() < 1) {
            return PriceInfo.builder()
                .standardPrice(0)
                .highPrice(0)
                .lowPrice(0)
                .transactionAmount(0)
                .build();
        }
        return priceInfos.get(0);
    }
}
