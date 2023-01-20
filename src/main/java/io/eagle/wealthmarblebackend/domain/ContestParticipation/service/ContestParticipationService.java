package io.eagle.wealthmarblebackend.domain.ContestParticipation.service;

import io.eagle.wealthmarblebackend.domain.ContestParticipation.dto.HistoryCahootsDto;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.dto.HistoryCahootsDtoList;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import io.eagle.wealthmarblebackend.domain.vacation.repository.VacationRepository;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestParticipationService {
    private final VacationRepository vacationRepository;
    private final ContestParticipationRepository contestParticipationRepository;


    @Transactional
    public void participate(Long cahootsId, Integer stocks){
        if (stocks > 10000) { // 주식 너무 많으면 reject
           throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
        // TODO : 사용자 정보에서 현재 잔액 가져오기
        Integer cache = 100000;
        Long userId = 3L;

        Vacation vacation = vacationRepository.findByIdAndStatus(cahootsId, VacationStatusType.CAHOOTS_ONGOING).orElseThrow(()-> new ApiException(ErrorCode.VACATION_NOT_FOUND));
        if ( cache < vacation.getStock().getPrice() * stocks) { // 공모 정보 가져올 때 진행중이 맞는지 체크하고 잔액이 모자라면 에러
            throw new ApiException(ErrorCode.USER_LACK_OF_CACHE);
        }
        // 사용자 정보에서 잔액 차감 + 공모 참여 현황에 추가
        // TODO : 사용자 잔액 차감
        ContestParticipation contestParticipation = new ContestParticipation(userId, vacation, stocks);
        contestParticipationRepository.save(contestParticipation);
    }

    public List<HistoryCahootsDto> getHistory(Long cahootsId) {
        // 공모 조회
        List<ContestParticipation> contestParticipation = contestParticipationRepository.findAllByCahootsId(cahootsId);
        // dto 형식으로 만들기
        return contestParticipation.stream().map(HistoryCahootsDto::toDto).collect(Collectors.toList());
    }
}
