package io.eagle.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfoVO {

    private Long vacationId;
    private Integer startPrice;
    private Integer highPrice;
    private Integer lowPrice;
    private Integer standardPrice;

    private Integer transactionAmount;
    private Integer transactionMoney;
}
