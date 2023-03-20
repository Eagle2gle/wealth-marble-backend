package io.eagle.domain.vacation.dto.response;

import io.eagle.domain.vacation.vo.MarketQueryVO;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
public class MarketRankDto {

    private Long vacationId = 0L;
    private String pictureUrl = "";
    private String title = "";
    private Integer currentPrice = 0;
    private Integer gap = 0;
    private Double gapRate = 0.0;
    private Double dividend = 0.0;
    private Double dividendRate = 0.0;

    public MarketRankDto(MarketQueryVO vo) {
        this.vacationId = vo.getVacationId().longValue();
        this.pictureUrl = vo.getPictureUrl();
        this.title = vo.getTitle();
        if (vo.getCurrentPrice() != null && vo.getStartPrice() != null) {
            this.currentPrice = vo.getCurrentPrice();
            this.gap = vo.getCurrentPrice() - vo.getStartPrice();
            this.gapRate = (vo.getCurrentPrice() - vo.getStartPrice()) * 100 / (double) vo.getStartPrice();
            this.dividend = (vo.getCurrentPrice() * 0.1 + Math.random() * 0.1);
            this.dividendRate = this.dividend / this.currentPrice;
        }
    }

}
