package io.eagle.domain.PriceInfo.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import io.eagle.domain.PriceInfo.dto.QChartResponseDto;
import lombok.RequiredArgsConstructor;

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
}
