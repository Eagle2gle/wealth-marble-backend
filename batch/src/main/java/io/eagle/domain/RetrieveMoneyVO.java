package io.eagle.domain;

import io.eagle.entity.type.ContestParticipationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveMoneyVO {

    private Long userId;
    private Integer addCash;
    private Long contestParticipationId;
    private String status;

}
