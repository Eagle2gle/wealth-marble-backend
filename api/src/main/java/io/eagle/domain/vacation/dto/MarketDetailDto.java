package io.eagle.domain.vacation.dto;

import io.eagle.entity.Picture;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class MarketDetailDto {

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
