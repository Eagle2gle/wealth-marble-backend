package io.eagle.domain;

import io.eagle.entity.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecentTransactionRequestVO {

    private Long vacationId;
    private Integer currentPrice;
    private LocalDateTime createdAt;

    public RecentTransactionRequestVO(Transaction transaction) {
        this.vacationId = transaction.getVacation().getId();
        this.currentPrice = transaction.getPrice();
        this.createdAt = transaction.getCreatedAt();
    }

}
