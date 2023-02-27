package io.eagle.domain.transaction.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecentTransactionRequestDto {

    private Long vacationId;
    private Integer currentPrice;
    private LocalDateTime createdAt;

}
