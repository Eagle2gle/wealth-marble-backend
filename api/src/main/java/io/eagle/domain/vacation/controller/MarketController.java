package io.eagle.domain.vacation.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.vacation.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/markets")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/{vacationId}")
    public ApiResponse getOne(@PathVariable("vacationId") Long vacationId) {
        return ApiResponse.createSuccess(marketService.getOne(vacationId));
    }

}
