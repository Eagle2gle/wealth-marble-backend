package io.eagle.domain.PriceInfo.service;

import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceInfoService {

    private final PriceInfoRepository priceInfoRepository;

    public List<ChartResponseDto> getVacationChartData(Long vacationId, ChartRequestDto chartRequestDto) {
        return priceInfoRepository.findAllByVacationOrderByCreatedAt(vacationId, chartRequestDto);
    }
}
