package io.eagle.wealthmarblebackend.domain.vacation.repository;

import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;

import java.util.List;

public interface VacationCustom {
    List<?> getVacationDetail(Long cahootsId);
    List<BreifCahootsDto> getVacationsBreif(VacationStatusType status, Integer offset);
}
