package io.eagle.domain.transaction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime date;
    private Integer price;
    private Integer amount;

    @QueryProjection
    public TransactionResponseDto(LocalDateTime date, Integer price, Integer amount) {
        this.date = date;
        this.price = price;
        this.amount = amount;
    }

}
