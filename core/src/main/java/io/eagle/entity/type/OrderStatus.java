package io.eagle.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    ONGOING("ONGOING"),
    DONE("DONE");

    private final String key;
}
