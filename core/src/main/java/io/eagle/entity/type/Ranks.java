package io.eagle.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Ranks {

    NAMJAK("NAMJAK"),
    JAJAK("JAJAK"),
    BAEKJAK("BAEKJAK"),
    WHOJAK("WHOJAK"),
    GONGJAK("GONGJAK");

    private final String key;
}
