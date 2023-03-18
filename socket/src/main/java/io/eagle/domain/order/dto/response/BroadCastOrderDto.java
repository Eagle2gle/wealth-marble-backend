package io.eagle.domain.order.dto.response;

import io.eagle.entity.type.OrderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BroadCastOrderDto {
    private Long marketId;
    private Integer price;
    private Integer amount;
    private OrderType orderType;

}
