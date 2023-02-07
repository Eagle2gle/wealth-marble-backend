package io.eagle.domain.PriceInfo.controller;

import io.eagle.domain.PriceInfo.service.PriceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/price/info")
@RequiredArgsConstructor
public class PriceInfoController {

    private final PriceInfoService priceInfoService;

}
