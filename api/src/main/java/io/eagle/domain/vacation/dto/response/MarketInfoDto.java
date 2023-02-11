package io.eagle.domain.vacation.dto.response;

import io.eagle.entity.Picture;
import io.eagle.entity.Vacation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MarketInfoDto {
    private String title;
    private String location;
    private String description;
    private List<String> pictures;

    public MarketInfoDto(Vacation vacation, List<String> pictures) {
        this.title = vacation.getTitle();
        this.location = vacation.getLocation();
        this.description = vacation.getDescription();
        this.pictures = pictures;
    }
}
