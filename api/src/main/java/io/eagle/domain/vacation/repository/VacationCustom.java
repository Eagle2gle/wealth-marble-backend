package io.eagle.domain.vacation.repository;

import io.eagle.domain.vacation.dto.*;
import io.eagle.domain.vacation.dto.response.*;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VacationCustom {
    DetailCahootsDto getVacationDetail(Long cahootsId);

    List<Long> findVacationIdByUserInterested(Long userId);
    List<BreifCahootsDto> getVacationsBreif(InfoConditionDto infoConditionDto);
    List<BreifV2CahootsDto> getVacationsBreifV2(InfoConditionDto infoConditionDto);
    List<LatestCahootsDto> findLatestVacations();
    List<Vacation> findAllMarkets(Pageable pageable, String keyword);
    List<ImminentInfoDto> findByImminentEndVacation();
    List<String> getCountries(VacationStatusType type);
    MarketRankDto findMarketRankInfoById(Long id);
    RecommendMarketDto getRecommendMarket(Long vacationId, Long userId);
    List<MarketRankDto> findTop5VacationByReward();
    List<MarketRankDto> findTop5VacationByTransaction();

}
