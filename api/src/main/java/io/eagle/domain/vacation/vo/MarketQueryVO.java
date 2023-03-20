package io.eagle.domain.vacation.vo;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class MarketQueryVO {

    private BigInteger vacationId;
    private String pictureUrl;
    private String title;
    private Integer currentPrice;
    private Integer startPrice;

    public MarketQueryVO(BigInteger vacationId, String pictureUrl, String title, Integer currentPrice,  Integer startPrice) {
        this.vacationId = vacationId;
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.currentPrice = currentPrice;
        this.startPrice = startPrice;
    }

    @Override
    public String toString() {
        return this.vacationId + " " + this.pictureUrl + " " + this.title + " " + this.currentPrice + " " + this.startPrice;
    }

}
