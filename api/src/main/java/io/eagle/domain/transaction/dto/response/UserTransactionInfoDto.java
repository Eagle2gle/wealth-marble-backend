package io.eagle.domain.transaction.dto.response;

import io.eagle.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public UserTransactionInfoDto(Transaction transaction) {

    }

}
