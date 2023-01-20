package io.eagle.wealthmarblebackend.domain.vacation.entity.type;

public enum ThemeLocationType {
    BEACH("BEACH"),
    FOREST("FOREST"),
    DOWNTOWN("DOWNTOWN");

    private String type;

    ThemeLocationType(String type) {
        this.type = type;
    }
}
