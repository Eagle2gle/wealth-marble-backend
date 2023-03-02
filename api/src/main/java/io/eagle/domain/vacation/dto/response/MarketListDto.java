package io.eagle.domain.vacation.dto.response;

import io.eagle.entity.type.PriceStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketListDto {

    private Long vacationId;
    private String picture;
    private String country;
    private String shortDescription;
    private Integer price;
    private PriceStatus priceStatus;

}
