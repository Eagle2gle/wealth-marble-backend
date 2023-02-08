package io.eagle.domain.PriceInfo.repository;

import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import io.eagle.entity.PriceInfo;

import java.util.List;

public interface PriceInfoRepositoryCustom {
    List<ChartResponseDto> findAllByVacationOrderByCreatedAt(Long vacationId, ChartRequestDto chartRequestDto);
    PriceInfo getYesterdayPriceInfo();
}
