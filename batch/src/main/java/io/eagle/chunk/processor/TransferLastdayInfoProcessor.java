package io.eagle.chunk.processor;

import io.eagle.entity.PriceInfo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class TransferLastdayInfoProcessor implements ItemProcessor<PriceInfo, PriceInfo> {
    private JdbcTemplate jdbcTemplate;

    public TransferLastdayInfoProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public PriceInfo process(PriceInfo lastPriceInfo) throws Exception {
        return PriceInfo.builder()
                .highPrice(lastPriceInfo.getStandardPrice())
                .lowPrice(lastPriceInfo.getStandardPrice())
                .standardPrice(lastPriceInfo.getStandardPrice())
                .startPrice(lastPriceInfo.getStandardPrice())
                .transactionAmount(0)
                .transactionMoney(0)
                .build();
    }
}
