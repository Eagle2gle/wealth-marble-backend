package io.eagle.domain.vacation.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarketRankingType {
    PRICE("PRICE"),
    PRICE_RATE("PRICE_RATE");

    private final String key;
}
