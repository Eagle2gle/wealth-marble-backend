package io.eagle.domain.vacation.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


@Data
@Builder
public class MarketInfoConditionDto {
    @Value("10")
    private Integer page;

    @Value("10")
    private Integer size;

    private String keyword;

}