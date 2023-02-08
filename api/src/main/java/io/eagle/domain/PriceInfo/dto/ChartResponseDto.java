package io.eagle.domain.PriceInfo.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChartResponseDto {

    private Date date;
    private Integer price;
    private Integer transactionAmount;

    @QueryProjection
    public ChartResponseDto(Date date, Integer price, Integer transactionAmount) {
        this.date = date;
        this.price = price;
        this.transactionAmount = transactionAmount;
    }

}
