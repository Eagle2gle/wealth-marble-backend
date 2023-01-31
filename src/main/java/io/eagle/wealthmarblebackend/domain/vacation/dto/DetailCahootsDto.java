package io.eagle.wealthmarblebackend.domain.vacation.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.ThemeBuildingType;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.ThemeLocationType;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DetailCahootsDto {

    private Long id;
    private String title;

    private ThemeLocationType themeLocation;

    private ThemeBuildingType themeBuilding;

    private String location;

    private Integer expectedMonth;

    private Long expectedTotalCost;

    @NotBlank
    private String shortDescription;

    @NotBlank
    private String descritption;

    private VacationStatusType status;

    private LocalDate stockStart;

    private LocalDate stockEnd;

    private Long stockPrice;

    private Integer stockNum;

    private Integer competitionRate;

    private Integer expectedRateOfReturn;

    // TODO : hashtag

    private List<String> images;

//    public static DetailCahootsDto toDto(Vacation vacation, Integer competitionRate) {
//        return DetailCahootsDto.builder()
//                .id(vacation.getId())
//                .title(vacation.getTitle())
//                .descritption(vacation.getDescritption())
//                .location(vacation.getLocation())
//                .expectedMonth(vacation.getPlan().getExpectedMonth())
//                .expectedTotalCost(vacation.getPlan().getExpectedTotalCost())
//                .shortDescription(vacation.getShortDescription())
//                .stockStart(vacation.getStockPeriod().getStart())
//                .stockEnd(vacation.getStockPeriod().getEnd())
//                .stockPrice(vacation.getStock().getPrice())
//                .competitionRate(competitionRate)
//                .themeLocation(vacation.getTheme().getThemeLocation())
//                .themeBuilding(vacation.getTheme().getThemeBuilding())
//                .status(vacation.getStatus())
//                .build();
//
//    }

    public DetailCahootsDto checkNull(){
        if(this.id == null) {
            throw new ApiException(ErrorCode.VACATION_NOT_FOUND);
        } else{
            return this;
        }
    }
}
