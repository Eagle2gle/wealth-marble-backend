package io.eagle.domain.order.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderHistoryVO {
    private Long id;
    private Integer price;
    private Integer amount;
}
