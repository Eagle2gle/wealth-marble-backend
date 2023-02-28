package io.eagle.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarketRankingType {
    PRICE("PRICE"),
    PRICE_RATE("PRICE_RATE"),

    COUNTRY("COUNTRY");

    private final String key;

    public static String CountryKey(String country){
        return COUNTRY + "-" + country;
    }
}
