package io.eagle.domain.interest.dto;

import io.eagle.entity.Interest;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InterestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long vacationId;

}
