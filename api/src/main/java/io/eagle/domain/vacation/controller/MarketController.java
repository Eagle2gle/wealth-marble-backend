package io.eagle.domain.vacation.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.vacation.dto.MarketInfoConditionDto;
import io.eagle.domain.vacation.service.MarketService;
import io.eagle.entity.type.MarketRankingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/markets")
    public ApiResponse getAllMarkets(MarketInfoConditionDto infoConditionDto) {
        return ApiResponse.createSuccess(marketService.getAllMarkets(infoConditionDto));
    }

    @GetMapping("/markets/{vacationId}")
    public ApiResponse getOne(@PathVariable("vacationId") Long vacationId) {
        return ApiResponse.createSuccess(marketService.getOne(vacationId));
    }

    @GetMapping("/markets/info/{vacationId}")
    public ApiResponse getInfo(@PathVariable("vacationId") Long vacationId) {
        return ApiResponse.createSuccess(marketService.getVacationInfo(vacationId));
    }

    @GetMapping("/markets/countries")
    public ApiResponse getCountries(){
        return ApiResponse.createSuccess(marketService.getCountries());
    }

    @GetMapping("/markets/rank")
    public ApiResponse getRankingPrice(
        @RequestParam(value = "type", defaultValue = "PRICE") String type,
        @RequestParam(value = "up", defaultValue = "TRUE") String up
    ) {
        return ApiResponse.createSuccess(
            marketService.getTop5MarketRankingByProperty(
                MarketRankingType.valueOf(type), Boolean.valueOf(up)
            )
        );
    }

    @GetMapping("/markets/recommend")
    public ApiResponse getRankingPrice(String country, Long userId) {
        return ApiResponse.createSuccess(marketService.getRecommendMarketByCountry(country, userId));
    }
}
