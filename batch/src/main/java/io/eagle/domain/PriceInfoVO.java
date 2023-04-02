package io.eagle.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfoVO {

    private Long vacationId;
    private LocalDateTime createdAt;
    private Integer startPrice;
    private Integer highPrice;
    private Integer lowPrice;
    private Integer standardPrice;

    private Integer transactionAmount;
    private Integer transactionMoney;
}
