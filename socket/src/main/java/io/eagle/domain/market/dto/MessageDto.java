package io.eagle.domain.market.dto;

import lombok.Data;

@Data
public class MessageDto {

    private Integer marketId;
    private Integer price;
    private Integer amount;
}
