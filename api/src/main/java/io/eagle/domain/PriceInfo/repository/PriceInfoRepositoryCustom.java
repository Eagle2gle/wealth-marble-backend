package io.eagle.domain.PriceInfo.repository;

import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;

import java.util.List;

public interface PriceInfoRepositoryCustom {
    List<ChartResponseDto> findAllByDateOrderByCreatedAt(ChartRequestDto chartRequestDto);
}
