package io.eagle.domain.vacation.vo;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class MarketQueryVO {

    private String pictureUrl;
    private String title;
    private Integer currentPrice;
    private Integer startPrice;

    public MarketQueryVO(String pictureUrl, String title, Integer currentPrice,  Integer startPrice) {
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.currentPrice = currentPrice;
        this.startPrice = startPrice;
    }

    @Override
    public String toString() {
        return this.pictureUrl + " " + this.title + " " + this.currentPrice + " " + this.startPrice;
    }

}
