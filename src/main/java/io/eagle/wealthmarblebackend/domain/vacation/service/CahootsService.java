package io.eagle.wealthmarblebackend.domain.vacation.service;

import io.eagle.wealthmarblebackend.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.wealthmarblebackend.domain.vacation.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.DetailCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.domain.vacation.repository.VacationRepository;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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
        Integer currentTotalStock = contestParticipationRepository.getCurrentContestNum(cahootsId).orElse(0);
        Integer competitionRate =  currentTotalStock * 100 / vacation.getStock().getNum();
        return DetailCahootsDto.toDto(vacation, competitionRate);
    }


}
