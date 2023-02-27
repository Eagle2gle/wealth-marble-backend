package io.eagle.domain.transaction.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecentTransactionResponseDto {

    private String pictureUrl = "";
    private String title = "";
    private Integer currentPrice = 0;
    private Integer gap = 0;
    private Double gapRate = 0.0;
    private Double dividend = 0.0;
    private Double dividendRate = 0.0;
    private LocalDateTime createdAt;

}
