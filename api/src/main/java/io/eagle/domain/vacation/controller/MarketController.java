package io.eagle.domain.vacation.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.vacation.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/markets/{vacationId}")
    public ApiResponse getOne(@PathVariable("vacationId") Long vacationId) {
        return ApiResponse.createSuccess(marketService.getOne(vacationId));
    }

    @GetMapping("/markets/info/{vacationId}")
    public ApiResponse getInfo(@PathVariable("vacationId") Long vacationId) {
        return ApiResponse.createSuccess(marketService.getVacationInfo(vacationId));
    }

}
