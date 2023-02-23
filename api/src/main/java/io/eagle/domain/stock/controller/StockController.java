package io.eagle.domain.stock.controller;

import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/auth/stocks/me")
    public ApiResponse getMineStock(@AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.createSuccess(stockService.getMineStock(authDetails.getUser()));
    }

}
