package io.eagle.domain.vacation.repository;

import io.eagle.domain.vacation.dto.*;

import java.util.List;

public interface VacationCustom {
    DetailCahootsDto getVacationDetail(Long cahootsId);
    List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto);
    List<BreifV2CahootsDto> getVacationsBreifV2(InfoConditionDto infoConditionDto);
    List<LatestCahootsDto> findLatestVacations();
}
