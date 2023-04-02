package io.eagle.chunk.processor;

import io.eagle.domain.PriceInfoVO;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Vacation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CopyPriceInfoProcessor implements ItemProcessor<PriceInfoVO, PriceInfo> {

    @Override
    public PriceInfo process(PriceInfoVO lastPriceInfo) throws Exception {
        log.debug("Vacation that didn't trade yesterday : " + lastPriceInfo.getVacationId());
        return PriceInfo.builder()
                .vacation(Vacation.builder().id(lastPriceInfo.getVacationId()).build())
                .highPrice(lastPriceInfo.getStandardPrice())
                .lowPrice(lastPriceInfo.getStandardPrice())
                .standardPrice(lastPriceInfo.getStandardPrice())
                .startPrice(lastPriceInfo.getStandardPrice())
                .createdAt(lastPriceInfo.getCreatedAt().plusDays(1))
                .transactionAmount(0)
                .transactionMoney(0)
                .build();
    }
}
