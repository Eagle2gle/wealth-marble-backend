package io.eagle.domain.vacation.dto;

import io.eagle.entity.type.VacationStatusType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
public class InfoConditionDto {
    private VacationStatusType[] types;
    @Min(value = 0, message = "page는 0 이상이어야합니다.")
    private Integer page;
    private String keyword;
}
