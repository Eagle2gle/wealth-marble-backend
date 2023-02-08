package io.eagle.domain.PriceInfo.service;

import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceInfoService {

    private final PriceInfoRepository priceInfoRepository;

}
