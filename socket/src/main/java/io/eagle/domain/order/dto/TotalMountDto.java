package io.eagle.domain.order.dto;

import io.eagle.entity.type.OrderType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TotalMountDto {
    private OrderType type;
    private Integer amount;
}
