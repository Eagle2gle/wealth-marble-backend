package io.eagle.domain.stock.repository;

import io.eagle.domain.stock.dto.StockInfoDto;

import java.util.List;

public interface StockRepositoryCustom {

    List<StockInfoDto> getTotalStockValueByUser(Long userId);

}
