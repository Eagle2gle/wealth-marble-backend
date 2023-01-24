package io.eagle.wealthmarblebackend.domain.vacation.repository;

import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifV2CahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.InfoConditionDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;

import java.util.List;

public interface VacationCustom {
    List<?> getVacationDetail(Long cahootsId);
    List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto);
    List<BreifV2CahootsDto> getVacationsBreifV2(InfoConditionDto infoConditionDto);
}
