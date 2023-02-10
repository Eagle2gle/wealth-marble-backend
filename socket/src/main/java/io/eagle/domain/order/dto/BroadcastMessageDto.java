package io.eagle.domain.order.dto;

import io.eagle.entity.type.OrderType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastMessageDto {
    private Long marketId;
    private Integer price;
    private Integer amount;

    private OrderType orderType;
}
