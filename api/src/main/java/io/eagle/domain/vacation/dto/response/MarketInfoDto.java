package io.eagle.domain.vacation.dto.response;

import io.eagle.entity.Vacation;
import lombok.Getter;

import java.util.List;

@Getter
public class MarketInfoDto {
    private Long vacationId;
    private String title;
    private String location;
    private String description;
    private List<String> pictures;

    public MarketInfoDto(Vacation vacation, List<String> pictures) {
        this.vacationId = vacation.getId();
        this.title = vacation.getTitle();
        this.location = vacation.getLocation();
        this.description = vacation.getDescription();
        this.pictures = pictures;
    }
}
