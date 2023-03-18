package io.eagle.domain.order.dto;

import io.eagle.entity.type.OrderType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockVO {
    private Long orderId;
    private Long userId;
    private Long marketId;
    private Integer price;
    private Integer amount;
    private OrderType orderType;
}
