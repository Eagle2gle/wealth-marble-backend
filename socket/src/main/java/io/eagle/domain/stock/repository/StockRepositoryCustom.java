package io.eagle.domain.stock.repository;

import io.eagle.entity.Stock;

public interface StockRepositoryCustom {
    Stock getUserCurrentStock(Long userId, Long vacationId);
}
