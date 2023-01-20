package io.eagle.wealthmarblebackend.domain.user.domain.type;

public enum ProviderType {
    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private String type;
    ProviderType(String type) { this.type = type; }
}
