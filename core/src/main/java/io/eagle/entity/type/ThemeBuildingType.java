package io.eagle.entity.type;

public enum ThemeBuildingType {
    VACATION_SPOT("VACATION_SPOT"),
    HOTEL("HOTEL"),
    GUEST_HOUSE("GUEST_HOUSE");

    private String type;

    ThemeBuildingType(String type) {
        this.type = type;
    }
}
