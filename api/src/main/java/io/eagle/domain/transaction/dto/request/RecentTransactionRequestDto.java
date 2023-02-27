package io.eagle.domain.transaction.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecentTransactionRequestDto {

    private Long vacationId;
    private Integer currentPrice;
    private LocalDateTime createdAt;

}
