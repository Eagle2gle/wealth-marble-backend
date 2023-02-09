package io.eagle.domain.order.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastMessageDto {
    private Long marketId;
    private Integer price;
    private Integer amount;
}
