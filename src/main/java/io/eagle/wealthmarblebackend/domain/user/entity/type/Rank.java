package io.eagle.wealthmarblebackend.domain.user.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rank {

    NAMJAK("NAMJAK"),
    JAJAK("JAJAK"),
    BAEKJAK("BAEKJAK"),
    WHOJAK("WHOJAK"),
    GONGJAK("GONGJAK");

    private final String key;
}
