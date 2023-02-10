package io.eagle.domain.PriceInfo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ChartTotalDto {

    private LocalDate standardDate;
    private Integer standardPrice;
    private Integer highPrice;
    private Integer lowPrice;
    private Integer transactionAmount;
    private List<ChartResponseDto> chartResponseDtos;

}
