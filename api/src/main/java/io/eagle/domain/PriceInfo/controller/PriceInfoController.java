package io.eagle.domain.PriceInfo.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.service.PriceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PriceInfoController {

    private final PriceInfoService priceInfoService;

    @GetMapping("/price/info/chart/{vacationId}")
    public ApiResponse getChartData(
        @PathVariable("vacationId") Long vacationId,
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate
        ) {
        return ApiResponse.createSuccess(priceInfoService.getVacationChartData(
            vacationId, new ChartRequestDto(startDate, endDate)
        ));
    }
}
