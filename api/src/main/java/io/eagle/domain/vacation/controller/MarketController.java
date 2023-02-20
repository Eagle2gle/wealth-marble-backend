package io.eagle.domain.vacation.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.vacation.service.MarketService;
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
    public ApiResponse getAllMarkets(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.createSuccess(marketService.getAllMarkets(pageable));
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

}
