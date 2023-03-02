package io.eagle.domain.interest.dto;

import io.eagle.entity.Picture;
import io.eagle.entity.Vacation;
import lombok.Getter;

@Getter
public class InterestInfoDto {

    private Long vacationId;
    private String title;
    private String shortDescription;
    private String location;
    private Picture picture;

    public InterestInfoDto(Vacation vacation) {
        this.vacationId = vacation.getId();
        this.title = vacation.getTitle();
        this.shortDescription = vacation.getShortDescription();
        this.location = vacation.getLocation();
        this.picture = vacation.getPictureList() != null && vacation.getPictureList().size() > 0 ? vacation.getPictureList().get(0) : null;
    }

}
