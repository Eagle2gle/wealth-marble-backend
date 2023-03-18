package io.eagle.domain.stock.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.entity.Stock;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.eagle.entity.QStock.*;

@RequiredArgsConstructor
public class StockRepositoryCustomImpl implements StockRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    public Stock getUserCurrentStock(Long userId, Long vacationId){
        return jpqlQueryFactory
            .selectFrom(stock)
            .where(stock.vacation.id.eq(vacationId), stock.user.id.eq(userId))
            .fetchOne();
    }

}
