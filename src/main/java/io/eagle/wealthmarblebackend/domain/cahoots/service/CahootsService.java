package io.eagle.wealthmarblebackend.domain.cahoots.service;

import io.eagle.wealthmarblebackend.domain.cahoots.domain.Cahoots;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.CahootsRepository;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CahootsService {

    private final CahootsRepository cahootsRepository;
    public void create(CreateCahootsDto createCahootsDto) {
        validateCahootsPeriod(createCahootsDto);
        // TODO : 사진 업로드
        // TODO : 요청 사용자의 정보 추가
        Cahoots newCahoots = new Cahoots(createCahootsDto);
        cahootsRepository.save(newCahoots);
    }

    /*
    * 공모 시작 및 마감 날짜 검증
    * */
    private void validateCahootsPeriod(CreateCahootsDto createCahootsDto){
        LocalDate today = LocalDate.now();
        if(
                createCahootsDto.getStockStart().compareTo(createCahootsDto.getStockEnd()) > 0
                || today.compareTo(createCahootsDto.getStockStart()) > 0
                || today.compareTo(createCahootsDto.getStockEnd()) > 0
        ) {
            throw new ApiException(ErrorCode.VACATION_PERIOD_INVALID);
        }
    }
}
