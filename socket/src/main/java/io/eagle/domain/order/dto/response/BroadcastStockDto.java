package io.eagle.domain.order.dto.response;

import io.eagle.entity.type.OrderType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastStockDto {
    private Long marketId;
    private Integer price;
    private Integer amount;

    private OrderType orderType;
}
