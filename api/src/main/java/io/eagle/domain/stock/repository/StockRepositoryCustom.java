package io.eagle.domain.stock.repository;

import io.eagle.domain.stock.dto.StockInfoDto;
import io.eagle.domain.stock.dto.response.StockMineDto;

import java.util.List;

public interface StockRepositoryCustom {

    List<StockInfoDto> getTotalStockValueByUser(Long userId);
    List<StockMineDto> getMineStockByUser(Long userId);

}
