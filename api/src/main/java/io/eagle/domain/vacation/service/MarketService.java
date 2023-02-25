package io.eagle.domain.vacation.service;

import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.dto.response.MarketDetailDto;
import io.eagle.domain.vacation.dto.response.MarketInfoDto;
import io.eagle.domain.vacation.dto.response.MarketListDto;
import io.eagle.domain.vacation.dto.response.MarketRankDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.type.MarketRankingType;
import io.eagle.entity.Picture;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Transaction;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.PriceStatus;
import io.eagle.entity.type.VacationStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketService {

    private final VacationRepository vacationRepository;
    private final TransactionRepository transactionRepository;
    private final PictureRepository pictureRepository;
    private final InterestRepository interestRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public List<MarketListDto> getAllMarkets(Pageable pageable) {
        return vacationRepository.findAllMarkets(pageable).stream().map(vacation -> {
            PriceInfo priceInfo = vacation.getPriceInfos() != null ? vacation.getPriceInfos().get(0) : null;
            Picture picture = vacation.getPictureList() != null ? vacation.getPictureList().get(0) : null;
            PriceStatus priceStatus = getPriceStatus(Optional.ofNullable(priceInfo));
            return MarketListDto
                .builder()
                .country(vacation.getCountry())
                .shortDescription(vacation.getShortDescription())
                .picture(Objects.requireNonNull(picture).getUrl())
                .price(Objects.requireNonNull(priceInfo).getStandardPrice())
                .priceStatus(priceStatus)
                .build();
        }).collect(Collectors.toList());
    }

    public MarketDetailDto getOne(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId).orElse(null);
        if (vacation != null) {
            Transaction transaction = transactionRepository.findByVacation(vacationId);
            List<String> pictures = pictureRepository.findUrlsByCahootsId(vacationId);
            List<Long> userIds = interestRepository.findAllByVacation(vacationId)
                .stream()
                .map(interest -> interest.getUser().getId())
                .collect(Collectors.toList());
            return MarketDetailDto
                .builder()
                .title(vacation.getTitle())
                .location(vacation.getLocation())
                .shortDescription(vacation.getShortDescription())
                .expectedRateOfReturn(vacation.getExpectedRateOfReturn())
                .price(transaction.getPrice())
                .pictures(pictures)
                .userIds(userIds)
                .build();
        }
        return null;
    }

    public MarketInfoDto getVacationInfo(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId).orElse(null);
        if (vacation != null) {
            List<String> pictures = pictureRepository.findUrlsByCahootsId(vacationId);
            return new MarketInfoDto(vacation, pictures);
        }
        return null;
    }

    private PriceStatus getPriceStatus(Optional<PriceInfo> priceInfo) {
        if (priceInfo.isEmpty()) {
            return PriceStatus.SAME;
        }

        Integer startPrice = priceInfo.get().getStartPrice();
        Integer standardPrice = priceInfo.get().getStandardPrice();

        if (startPrice > standardPrice) {
            return PriceStatus.DOWN;
        } else if (startPrice.equals(standardPrice)) {
            return PriceStatus.SAME;
        } else {
            return PriceStatus.UP;
        }
    }

    public List<String> getCountries(){
        return vacationRepository.getCountries(VacationStatusType.MARKET_ONGOING);
    }

    public List<MarketRankDto> getTop5MarketRankingByProperty(MarketRankingType type, Boolean upOrDown) {
        return this.findMarketRankInRedis(type.getKey(), upOrDown)
            .stream()
            .map(e -> this.findMarketRankInfoById(
                Long.parseLong(Objects.requireNonNull(e.getValue())))
            )
            .collect(Collectors.toList());
    }

    private Set<ZSetOperations.TypedTuple<String>> findMarketRankInRedis(String key, Boolean upOrDown) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        if (upOrDown) {
            return zSetOperations.reverseRangeWithScores(key, 0, 5);
        }
        return zSetOperations.rangeWithScores(key, 0, 5);
    }

    private MarketRankDto findMarketRankInfoById(Long id) {
        return vacationRepository.findMarketRankInfoById(id);
    }
}
