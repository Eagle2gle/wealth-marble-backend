package io.eagle.wealthmarblebackend.domain.vacation.repository;

import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.DetailCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifV2CahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.InfoConditionDto;

import java.util.List;

public interface VacationCustom {
    DetailCahootsDto getVacationDetail(Long cahootsId);
    List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto);
    List<BreifV2CahootsDto> getVacationsBreifV2(InfoConditionDto infoConditionDto);
}
