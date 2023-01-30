package io.eagle.wealthmarblebackend.domain.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionInfoDto {

    private String vacationName;
    private String transactionTime;
    private Integer price;
    private Integer amount;
    private String transactionType;

}
