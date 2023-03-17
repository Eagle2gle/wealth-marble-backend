package io.eagle.domain.vacation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecommendMarketDto {
    private Long id;
    private String title;
    private Integer expectedRateOfReturn;

    private String image;
    private Boolean isInterest;
}
