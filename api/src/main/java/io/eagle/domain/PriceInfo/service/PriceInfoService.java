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
        PriceInfo yesterdayInfo = this.getYesterdayInfo();
        return ChartTotalDto
            .builder()
            .standardDate(yesterdayInfo.getCreatedAt() != null ? yesterdayInfo.getCreatedAt().toLocalDate() : null)
            .standardPrice(yesterdayInfo.getStandardPrice())
            .highPrice(yesterdayInfo.getHighPrice())
            .lowPrice(yesterdayInfo.getLowPrice())
            .transactionAmount(yesterdayInfo.getTransactionAmount())
            .chartResponseDtos(chartResponseDtos)
            .build();
    }

    private PriceInfo getYesterdayInfo() {
        PriceInfo yesterdayInfo = priceInfoRepository.getYesterdayPriceInfo();
        if (yesterdayInfo != null) {
            return yesterdayInfo;
        }
        return PriceInfo.builder()
            .standardPrice(0)
            .highPrice(0)
            .lowPrice(0)
            .transactionAmount(0)
            .build();
    }
}
