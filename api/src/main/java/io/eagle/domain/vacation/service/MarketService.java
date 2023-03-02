package io.eagle.domain.vacation.service;

import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.dto.MarketInfoConditionDto;
import io.eagle.domain.vacation.dto.response.*;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.type.MarketRankingType;
import io.eagle.entity.Picture;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Transaction;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.PriceStatus;
import io.eagle.entity.type.VacationStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.eagle.entity.type.MarketRankingType.CountryKey;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketService {

    private final VacationRepository vacationRepository;
    private final TransactionRepository transactionRepository;
    private final PictureRepository pictureRepository;
    private final InterestRepository interestRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> redisZSet;

    @PostConstruct
    public void init(){
        this.redisZSet = redisTemplate.opsForZSet();
    }

    public List<MarketListDto> getAllMarkets(MarketInfoConditionDto infoConditionDto) {
        Pageable pageable = PageRequest.of(infoConditionDto.getPage(), infoConditionDto.getSize());
        return vacationRepository.findAllMarkets(pageable, infoConditionDto.getKeyword()).stream().map(vacation -> {
            PriceInfo priceInfo = vacation.getPriceInfos() != null && vacation.getPriceInfos().size() > 0 ? vacation.getPriceInfos().get(0) : null;
            String picture = vacation.getPictureList() != null && vacation.getPictureList().size() > 0 ? vacation.getPictureList().get(0).getUrl() : null;
            PriceStatus priceStatus = getPriceStatus(priceInfo);
            return MarketListDto
                .builder()
                .vacationId(vacation.getId())
                .country(vacation.getCountry())
                .shortDescription(vacation.getShortDescription())
                .picture(picture)
                .price(priceInfo != null ? priceInfo.getStandardPrice() : vacation.getStock().getPrice().intValue())
                .priceStatus(priceStatus)
                .build();
        }).collect(Collectors.toList());
    }

    public MarketDetailDto getOne(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId).orElse(null);
        if (vacation != null) {
            Transaction transaction = transactionRepository.findOneByVacation(vacationId);
            List<String> pictures = pictureRepository.findUrlsByCahootsId(vacationId);
            List<Long> userIds = interestRepository.findAllByVacation(vacationId)
                .stream()
                .map(interest -> interest.getUser().getId())
                .collect(Collectors.toList());
            return MarketDetailDto
                .builder()
                .vacationId(vacationId)
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

    private PriceStatus getPriceStatus(PriceInfo priceInfo) {
        if (priceInfo == null) {
            return PriceStatus.SAME;
        }

        Integer startPrice = priceInfo.getStartPrice();
        Integer standardPrice = priceInfo.getStandardPrice();

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
                Long.parseLong(Objects.requireNonNull(e)))
            )
            .collect(Collectors.toList());
    }

    public List<RecommendMarketDto> getRecommendMarketByCountry(String country, Long userId){
        String key = CountryKey(country);
        return this.findMarketRankInRedis(key, true)
                .stream()
                .map(e -> vacationRepository.getRecommendMarket(Long.parseLong(Objects.requireNonNull(e)), userId))
                .collect(Collectors.toList());
    }

    private Set<String> findMarketRankInRedis(String key, Boolean upOrDown) {
        if (upOrDown) {
            return redisZSet.reverseRange(key, 0, 5);
        }
        return redisZSet.range(key, 0, 5);
    }

    private MarketRankDto findMarketRankInfoById(Long id) {
        return vacationRepository.findMarketRankInfoById(id);
    }
}
