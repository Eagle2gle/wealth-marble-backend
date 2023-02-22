package io.eagle.domain.ContestParticipation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContestParticipationMineDto {

    private String title;
    private LocalDateTime createdAt;
    private Integer price;
    private Integer amount;
    private String status;

}
