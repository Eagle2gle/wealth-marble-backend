package io.eagle.domain.stock.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class StockInfoDto {

    private Integer price;
    private Integer amount;

    public StockInfoDto(BigInteger price, BigDecimal amount) {
        this.price = price.intValue();
        this.amount = amount.intValue();
    }
}
