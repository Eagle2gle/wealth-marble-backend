package io.eagle.chunk.processor;

import io.eagle.domain.PriceInfoVO;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Vacation;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class InitPriceInfoProcessor implements ItemProcessor<Vacation, PriceInfo> {

    @Override
    public PriceInfo process(Vacation vacation) throws Exception {
        return PriceInfo.builder()
                .vacation(Vacation.builder().id(vacation.getId()).build())
                .highPrice(vacation.getStock().getPrice().intValue())
                .lowPrice(vacation.getStock().getPrice().intValue())
                .standardPrice(vacation.getStock().getPrice().intValue())
                .startPrice(vacation.getStock().getPrice().intValue())
                .createdAt(LocalDateTime.now().minusDays(1L))
                .transactionAmount(0)
                .transactionMoney(0)
                .build();
    }
}

