package io.eagle.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PriceStatus {

    SAME("SAME"),
    UP("UP"),
    DOWN("DOWN");

    private final String key;
}
