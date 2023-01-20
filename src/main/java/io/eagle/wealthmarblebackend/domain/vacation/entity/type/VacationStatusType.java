package io.eagle.wealthmarblebackend.domain.cahoots.domain.type;

public enum VacationStatusType {
    CAHOOTS_BEFORE("CAHOOTS_BEFORE"),
    CAHOOTS_ONGOING("CAHOOTS_ONGOING"),
    CAHOOTS_OPEN("CAHOOTS_OPEN"),
    CAHOOTS_CLOSE("CAHOOTS_CLOSE");
    private String type;

    VacationStatusType(String type) {
        this.type = type;
    }
}
