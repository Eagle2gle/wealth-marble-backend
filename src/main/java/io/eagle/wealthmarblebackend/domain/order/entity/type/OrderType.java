package io.eagle.wealthmarblebackend.domain.order.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {

    BUY("BUY"),
    SELL("SELL");

    private final String key;
}
