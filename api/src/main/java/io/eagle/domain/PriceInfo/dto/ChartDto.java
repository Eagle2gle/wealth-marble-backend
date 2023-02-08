package io.eagle.domain.PriceInfo.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChartDto {

    private Date date;
    private Integer price;
    private Integer transactionAmount;

    @QueryProjection
    public ChartDto(Date date, Integer price, Integer transactionAmount) {
        this.date = date;
        this.price = price;
        this.transactionAmount = transactionAmount;
    }

}
