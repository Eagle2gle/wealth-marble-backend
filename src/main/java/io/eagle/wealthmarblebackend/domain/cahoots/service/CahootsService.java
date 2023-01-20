package io.eagle.wealthmarblebackend.domain.cahoots.service;

import io.eagle.wealthmarblebackend.domain.cahoots.domain.ContestParticipation.ContestParticipationRepository;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.VacationRepository;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.Vacation;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.DetailCahootsDto;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CahootsService {

    private final VacationRepository vacationRepository;
    private final ContestParticipationRepository contestParticipationRepository;

    public void create(CreateCahootsDto createCahootsDto) {
        createCahootsDto.validateCahootsPeriod();
        // TODO : 사진 업로드
        // TODO : 요청 사용자의 정보 추가
        Vacation newVacation = new Vacation(createCahootsDto);
        vacationRepository.save(newVacation);
    }

    public DetailCahootsDto getDetail(Long cahootsId) {
        Vacation vacation = vacationRepository.findById(cahootsId).orElseThrow(()-> new ApiException(ErrorCode.VACATION_NOT_FOUND));
        Integer currentTotalStock = contestParticipationRepository.getCurrentContestNum(cahootsId).get(0);
        Integer competitionRate = currentTotalStock * 100 / vacation.getStock().getNum();
        return DetailCahootsDto.toDto(vacation, competitionRate);
        Vacation newCahoots = new Vacation(createCahootsDto);
        cahootsRepository.save(newCahoots);
    }


}
