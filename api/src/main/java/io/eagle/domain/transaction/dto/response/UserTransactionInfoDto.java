package io.eagle.domain.transaction.dto.response;

import io.eagle.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

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

    public UserTransactionInfoDto(Transaction transaction, Long orderId) {
        this.transactionType = transaction.getBuyOrder().getId().equals(orderId) ? "BUY" : "SELL";
        this.vacationName = transaction.getVacation().getTitle();
        this.transactionTime = transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("YY-MM-DD"));
        this.price = transaction.getPrice();
        this.amount = transaction.getAmount();
    }

}
