package io.eagle.domain;

import io.eagle.entity.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class RecentTransactionRequestVO {

    private Long vacationId;
    private Integer currentPrice;
    private LocalDateTime createdAt;

    public RecentTransactionRequestVO(Long vacationId, Integer currentPrice, String createdAt) {
        String[] created = createdAt.split("\\.");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(created[0], formatter);
        this.vacationId = vacationId;
        this.currentPrice = currentPrice;
        this.createdAt = dateTime;
    }

}
