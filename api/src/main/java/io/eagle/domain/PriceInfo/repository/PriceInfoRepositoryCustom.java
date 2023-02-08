package io.eagle.domain.PriceInfo.repository;

import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PriceInfoRepositoryCustom {
    Page<ChartResponseDto> findAllByDateOrderByCreatedAt(Pageable pageable, ChartRequestDto chartRequestDto);
}
