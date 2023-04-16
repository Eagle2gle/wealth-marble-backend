package io.eagle.domain;

import io.eagle.entity.type.ContestParticipationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContestParticipationVO {

    private Long id;
    private Long userId;
    private Long vacationId;
    private Integer stocks;
    private ContestParticipationStatus status;

}
