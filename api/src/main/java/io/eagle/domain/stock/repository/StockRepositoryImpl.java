package io.eagle.domain.stock.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.stock.dto.QStockInfoDto;
import io.eagle.domain.stock.dto.StockInfoDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.eagle.entity.QStock.*;

@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<StockInfoDto> getTotalStockValueByUser(Long userId) {
        return jpqlQueryFactory
            .select(new QStockInfoDto(stock.price, stock.amount))
            .from(stock)
            .where(stock.user.id.eq(userId))
            .fetch();
    }
}
