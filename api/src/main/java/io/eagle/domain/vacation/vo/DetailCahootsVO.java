package io.eagle.domain.vacation.vo;

import io.eagle.entity.type.ThemeBuildingType;
import io.eagle.entity.type.ThemeLocationType;
import io.eagle.entity.type.VacationStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class DetailCahootsVO {
    private Long id;
    private String title;

    private ThemeLocationType themeLocation;

    private ThemeBuildingType themeBuilding;

    private String country;
    private String location;

    private Integer expectedMonth;

    private Long expectedTotalCost;

    @NotBlank
    private String shortDescription;

    @NotBlank
    private String description;

    private VacationStatusType status;

    private LocalDate stockStart;

    private LocalDate stockEnd;

    private Long stockPrice;

    private Integer stockNum;

    private Integer competitionRate;

    private Integer expectedRateOfReturn;
    private String images;

    private Integer interestCount;

    private Boolean isInterest;
}
