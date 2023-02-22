package io.eagle.domain.stock.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockInfoDto {

    private Integer price;
    private Integer amount;

    @QueryProjection
    public StockInfoDto(Integer price, Integer amount) {
        this.price = price;
        this.amount = amount;
    }

}
