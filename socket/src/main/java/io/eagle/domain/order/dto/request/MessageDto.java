package io.eagle.domain.order.dto.request;

import io.eagle.entity.Order;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {

    private Long marketId;
    private Integer price;
    private Integer amount;
    private OrderType orderType;

    private Long requesterId;

    public Order buildOrder(User user, Vacation vacation, Integer existingAmount, OrderStatus status){
        return Order.builder()
                .amount(Math.min(this.getAmount(), existingAmount))
                .status(status)
                .orderType(this.getOrderType())
                .price(this.getPrice())
                .user(user)
                .vacation(vacation)
//                .vacationId(this.getMarketId())
                .build();
    }
}
