package io.eagle.domain.PriceInfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChartResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime date;
    private Integer price;
    private Integer transactionAmount;

    @QueryProjection
    public ChartResponseDto(LocalDateTime date, Integer price, Integer transactionAmount) {
        this.date = date;
        this.price = price;
        this.transactionAmount = transactionAmount;
    }

}
