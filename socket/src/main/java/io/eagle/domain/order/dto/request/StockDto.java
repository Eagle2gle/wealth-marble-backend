package io.eagle.domain.order.dto.request;

import io.eagle.entity.Order;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class StockDto {

    private Long marketId;
    private Integer price;
    private Integer amount;
    private OrderType orderType;

    @NotNull
    private Long requesterId;
    private String token;

    public Order buildOrder(User user, Vacation vacation, Integer existingAmount, OrderStatus status){
        return Order.builder()
                .amount(Math.min(this.getAmount(), existingAmount))
                .status(status)
                .orderType(this.getOrderType())
                .price(this.getPrice())
                .user(user)
                .vacation(vacation)
                .build();
    }
}
