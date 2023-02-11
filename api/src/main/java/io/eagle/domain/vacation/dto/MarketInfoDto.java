package io.eagle.domain.vacation.dto;

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

    public MarketInfoDto(Vacation vacation) {
        this.title = vacation.getTitle();
        this.location = vacation.getLocation();
        this.description = vacation.getDescription();
        this.pictures = vacation.getPictureList() != null ? vacation.getPictureList().stream().map(Picture::getUrl).collect(Collectors.toList()) : null;
    }
}
