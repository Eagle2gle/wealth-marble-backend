package io.eagle.domain.vacation.dto.response;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class MarketDetailDto {

    private Long vacationId;
    @NotNull
    private String title;
    @NotNull
    private String location;
    @NotNull
    private String shortDescription;
    @NotNull
    private Integer expectedRateOfReturn;
    @NotNull
    private Integer price;
    private List<String> pictures;
    private List<Long> userIds;

}
