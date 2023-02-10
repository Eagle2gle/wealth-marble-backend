package io.eagle.domain.PriceInfo.service;

import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import io.eagle.domain.PriceInfo.dto.ChartTotalDto;
import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import io.eagle.entity.PriceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceInfoService {

    private final PriceInfoRepository priceInfoRepository;

    public ChartTotalDto getVacationChartData(Long vacationId, ChartRequestDto chartRequestDto) {
        List<ChartResponseDto> chartResponseDtos = priceInfoRepository.findAllByVacationOrderByCreatedAt(vacationId, chartRequestDto);
        PriceInfo yesterdayInfo = priceInfoRepository.getYesterdayPriceInfo();
        return ChartTotalDto
            .builder()
            .standardDate(yesterdayInfo.getCreatedAt().toLocalDate())
            .standardPrice(yesterdayInfo.getStandardPrice())
            .highPrice(yesterdayInfo.getHighPrice())
            .lowPrice(yesterdayInfo.getLowPrice())
            .transactionAmount(yesterdayInfo.getTransactionAmount())
            .chartResponseDtos(chartResponseDtos)
            .build();
    }
}
