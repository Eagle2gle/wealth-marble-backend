package io.eagle.domain.ContestParticipation.service;

import io.eagle.domain.ContestParticipation.dto.HistoryCahootsDto;
import io.eagle.domain.ContestParticipation.dto.HistoryCahootsDtoList;
import io.eagle.domain.ContestParticipation.dto.response.ContestParticipationMineDto;
import io.eagle.entity.ContestParticipation;
import io.eagle.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.exception.ApiException;
import io.eagle.exception.error.ErrorCode;
import io.eagle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestParticipationService {

    private final UserRepository userRepository;
    private final VacationRepository vacationRepository;
    private final ContestParticipationRepository contestParticipationRepository;

    @Transactional
    public void participate(Long cahootsId, Integer stocks, User user){
        if (stocks > 10000) { // 주식 너무 많으면 reject
           throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
        Vacation vacation = vacationRepository.findByIdAndStatus(cahootsId, VacationStatusType.CAHOOTS_ONGOING).orElseThrow(()-> new ApiException(ErrorCode.VACATION_NOT_FOUND));
        Long cash = user.getCash();
        Long stockTotalPrice = vacation.getStock().getPrice() * stocks;
        verifyUserCash(cash, stockTotalPrice);

        // 사용자 정보에서 잔액 차감 + 공모 참여 현황에 추가
        subtractUserCash(user, stockTotalPrice);
        ContestParticipation contestParticipation = ContestParticipation.builder().user(user).vacation(vacation).stocks(stocks).build();
        contestParticipationRepository.save(contestParticipation);
    }

    public HistoryCahootsDtoList getHistory(Long cahootsId) {
        // 공모 조회
        List<ContestParticipation> contestParticipation = contestParticipationRepository.findAllByCahootsId(cahootsId);
        // dto 형식으로 만들기
        return HistoryCahootsDtoList.builder().result(contestParticipation.stream().map(HistoryCahootsDto::toDto).collect(Collectors.toList())).build();
    }

    public List<ContestParticipationMineDto> getMyContestParticipation(User user) {
        List<ContestParticipation> contestParticipations = contestParticipationRepository.findAllByUserId(user.getId());
        return contestParticipations.stream().map(contestParticipation -> {
            Vacation vacation = contestParticipation.getVacation();
            if (vacation != null) {
                return ContestParticipationMineDto
                    .builder()
                    .title(vacation.getTitle())
                    .createdAt(contestParticipation.getCreatedAt())
                    .amount(contestParticipation.getStocks())
                    .price(vacation.getStock().getPrice().intValue())
                    .status(vacation.getStatus().toString())
                    .build();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void verifyUserCash(Long userCash, Long stockTotalPrice){
        if(userCash < stockTotalPrice){ // 공모 정보 가져올 때 진행중이 맞는지 체크하고 잔액이 모자라면 에러
            throw new ApiException(ErrorCode.USER_LACK_OF_CACHE);
        }
    }

    private void subtractUserCash(User user, Long stockTotalPrice){
        Long leftCash = user.getCash() - stockTotalPrice;
        user.setCash(leftCash);
        userRepository.updateCash(user.getId(), user.getCash());
    }
}
