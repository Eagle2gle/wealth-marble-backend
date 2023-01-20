package io.eagle.wealthmarblebackend.domain.user.domain.type;

public enum Rank {

    NAMJAK("NAMJAK"),
    JAJAK("JAJAK"),
    BAEKJAK("BAEKJAK"),
    WHOJAK("WHOJAK"),
    GONGJAK("GONGJAK");

    private String type;

    Rank(String type) { this.type = type; }
}
