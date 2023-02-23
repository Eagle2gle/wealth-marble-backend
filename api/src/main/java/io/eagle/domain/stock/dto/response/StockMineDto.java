package io.eagle.domain.stock.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockMineDto {

    private String title;
    private Double profitRate;
    private Integer currentPrice;
    private Double pricePerStock;
    private Integer totalAmount;

    @QueryProjection
    public StockMineDto(String title, Double profitRate, Integer currentPrice, Double pricePerStock, Integer totalAmount) {
        this.title = title;
        this.profitRate = profitRate;
        this.currentPrice = currentPrice;
        this.pricePerStock = pricePerStock;
        this.totalAmount = totalAmount;
    }

}
