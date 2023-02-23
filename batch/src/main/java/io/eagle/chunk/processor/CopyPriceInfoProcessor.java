package io.eagle.chunk.processor;

import io.eagle.domain.PriceInfoVO;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Vacation;
import org.springframework.batch.item.ItemProcessor;

public class CopyPriceInfoProcessor implements ItemProcessor<PriceInfoVO, PriceInfo> {

    @Override
    public PriceInfo process(PriceInfoVO lastPriceInfo) throws Exception {
        return PriceInfo.builder()
                .vacation(Vacation.builder().id(lastPriceInfo.getVacationId()).build())
                .highPrice(lastPriceInfo.getStandardPrice())
                .lowPrice(lastPriceInfo.getStandardPrice())
                .standardPrice(lastPriceInfo.getStandardPrice())
                .startPrice(lastPriceInfo.getStandardPrice())
                .transactionAmount(0)
                .transactionMoney(0)
                .build();
    }
}
