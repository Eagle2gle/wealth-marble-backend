package io.eagle.wealthmarblebackend.domain.ContestParticipation.service;

import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import io.eagle.wealthmarblebackend.domain.vacation.repository.VacationRepository;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContestParticipationService {
    private final VacationRepository vacationRepository;
    private final ContestParticipationRepository contestParticipationRepository;

    //    @Transactional
    public void participate(Long cahootsId, Integer stocks){
        // TODO : 주식 너무 많으면 bad request 처리
        // TODO : 사용자 정보에서 현재 잔액 가져오기
        Integer cache = 100000;
        Long userId = 3L;
        // transaction 시작
        // 공모 정보 가져올 때 진행중이 맞는지 체크하고 잔액이 모자라면 에러
        Vacation vacation = vacationRepository.findByIdAndStatus(cahootsId, VacationStatusType.CAHOOTS_ONGOING).orElseThrow(()-> new ApiException(ErrorCode.VACATION_NOT_FOUND));
        if ( cache < vacation.getStock().getPrice() * stocks) {
            throw new ApiException(ErrorCode.USER_LACK_OF_CACHE);
        }
        // 사용자 정보에서 잔액 차감 + 공모 참여 현황에 추가
        ContestParticipation contestParticipation = new ContestParticipation(userId, vacation, stocks);
        contestParticipationRepository.save(contestParticipation);

    }
}
