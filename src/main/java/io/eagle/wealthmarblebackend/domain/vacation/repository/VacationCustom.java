package io.eagle.wealthmarblebackend.domain.vacation.repository;

import io.eagle.wealthmarblebackend.domain.vacation.dto.*;

import java.util.List;

public interface VacationCustom {
    DetailCahootsDto getVacationDetail(Long cahootsId);
    List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto);
    List<BreifV2CahootsDto> getVacationsBreifV2(InfoConditionDto infoConditionDto);
    List<LatestCahootsDto> findLatestVacations();
}
