package io.eagle.wealthmarblebackend.domain.cahoots.service;

import io.eagle.wealthmarblebackend.domain.cahoots.domain.VacationRepository;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.Vacation;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CahootsService {

    private final VacationRepository vacationRepository;
    public void create(CreateCahootsDto createCahootsDto) {
        createCahootsDto.validateCahootsPeriod();
        // TODO : 사진 업로드
        // TODO : 요청 사용자의 정보 추가
        Vacation newVacation = new Vacation(createCahootsDto);
        vacationRepository.save(newVacation);
    }


}
