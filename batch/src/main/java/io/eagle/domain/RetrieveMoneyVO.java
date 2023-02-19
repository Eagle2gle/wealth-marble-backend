package io.eagle.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveMoneyVO {

    private Long userId;
    private Integer addCash;
    private Long contestParticipationId;

}
