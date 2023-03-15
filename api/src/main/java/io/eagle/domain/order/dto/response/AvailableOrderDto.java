package io.eagle.domain.order.dto.response;

import io.eagle.entity.type.OrderType;
import lombok.Getter;

@Getter
public class AvailableOrderDto {
    public Integer price;
    public Integer amount;
    public OrderType orderType;
}
