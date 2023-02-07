package io.eagle.domain.interest.dto;

import io.eagle.entity.Interest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterestDto {

    private Long userId;
    private Long vacationId;

    public InterestDto(Long userId, Long vacationId) {
        this.userId = userId;
        this.vacationId = vacationId;
    }

    public InterestDto(Interest interest) {
        this.userId = interest.getUser().getId();
        this.vacationId = interest.getVacation().getId();
    }

}
