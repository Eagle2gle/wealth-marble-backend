package io.eagle.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContestParticipationStatus {

    PARTICIPATION("PARTICIPATION"),
    FAILED("FAILED");

    private final String key;

}
