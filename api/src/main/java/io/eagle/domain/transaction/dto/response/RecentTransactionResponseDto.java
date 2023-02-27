package io.eagle.domain.transaction.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecentTransactionResponseDto {

    private String pictureUrl;
    private String title;
    private Integer currentPrice;
    private Integer gap;
    private Double gapRate;
    private Double dividend;
    private Double dividendRate;
    private LocalDateTime createdAt;

}
