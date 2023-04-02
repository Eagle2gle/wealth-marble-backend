package io.eagle.chunk.processor;

import io.eagle.domain.PriceInfoVO;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Vacation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j

public class PriceInfoProcessor implements ItemProcessor<PriceInfoVO, PriceInfo> {

    @Override
    public PriceInfo process(PriceInfoVO lastPriceInfo) throws Exception {
        log.debug("Vacation that traded yesterday : " + lastPriceInfo.getVacationId());
        return PriceInfo.builder()
                .vacation(Vacation.builder().id(lastPriceInfo.getVacationId()).build())
                .highPrice(lastPriceInfo.getHighPrice())
                .lowPrice(lastPriceInfo.getLowPrice())
                .standardPrice(lastPriceInfo.getStandardPrice())
                .startPrice(lastPriceInfo.getStartPrice())
                .transactionAmount(lastPriceInfo.getTransactionAmount())
                .transactionMoney(lastPriceInfo.getTransactionMoney())
                .createdAt(lastPriceInfo.getCreatedAt())
                .build();
    }
}
