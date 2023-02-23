package io.eagle.domain.stock.vo;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class StockMineVO {

    private String title;
    private Integer currentPrice;
    private Integer totalPrice;
    private Integer totalAmount;

    public StockMineVO(String title, BigInteger currentPrice, BigDecimal totalPrice, BigDecimal totalAmount) {
        this.title = title;
        this.currentPrice = currentPrice.intValue();
        this.totalPrice = totalPrice.intValue();
        this.totalAmount = totalAmount.intValue();
    }

    @Override
    public String toString() {
        return title + " " + currentPrice + " " + totalPrice.toString() + " " + totalAmount.toString();
    }

}
