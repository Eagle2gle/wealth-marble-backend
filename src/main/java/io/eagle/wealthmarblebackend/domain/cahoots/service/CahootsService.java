package io.eagle.wealthmarblebackend.domain.cahoots.service;

import io.eagle.wealthmarblebackend.domain.cahoots.domain.Cahoots;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.CahootsRepository;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CahootsService {

    private final CahootsRepository cahootsRepository;
    public void create(CreateCahootsDto createCahootsDto) {
        // input 추가 검증
        validateCahootsPeriod(createCahootsDto);
        // TODO : 사진 업로드

        // entity 변환
        // TODO : 요청 사용자의 정보 추가
        Cahoots newCahoots = new Cahoots(createCahootsDto);
        // 저장
        cahootsRepository.save(newCahoots);
    }

    private void validateCahootsPeriod(CreateCahootsDto createCahootsDto){
        if(createCahootsDto.getStockStart().compareTo(createCahootsDto.getStockEnd()) > 0) {
            // throw exception
        }
    }
}
