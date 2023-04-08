package io.eagle.domain.vacation.dto.response;

import io.eagle.domain.vacation.vo.DetailCahootsVO;
import io.eagle.entity.type.ThemeBuildingType;
import io.eagle.entity.type.ThemeLocationType;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.exception.ApiException;
import io.eagle.exception.error.ErrorCode;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCahootsDto {

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

    private List<String> images;

    private Integer interestCount;

    private Boolean isInterest;

    public static DetailCahootsDto toDto(List<DetailCahootsVO> vacationDetailList) {
        if(vacationDetailList.size() == 0){
            throw new ApiException(ErrorCode.VACATION_NOT_FOUND);
        }
        DetailCahootsVO vd = vacationDetailList.get(0);
        List<String> images = vacationDetailList.stream().map(DetailCahootsVO::getImages).collect(Collectors.toList());

        return DetailCahootsDto.builder()
                .id(vd.getId())
                .title(vd.getTitle())
                .country(vd.getCountry())
                .description(vd.getDescription())
                .location(vd.getLocation())
                .expectedMonth(vd.getExpectedMonth())
                .expectedTotalCost(vd.getExpectedTotalCost())
                .shortDescription(vd.getShortDescription())
                .stockStart(vd.getStockStart())
                .stockEnd(vd.getStockEnd())
                .stockPrice(vd.getStockPrice())
                .stockNum(vd.getStockNum())
                .competitionRate(vd.getCompetitionRate())
                .expectedRateOfReturn(vd.getExpectedRateOfReturn())
                .themeLocation(vd.getThemeLocation())
                .themeBuilding(vd.getThemeBuilding())
                .status(vd.getStatus())
                .images(images)
                .interestCount(vd.getInterestCount())
                .isInterest(vd.getIsInterest())
                .build();
    }

    public DetailCahootsDto checkNull(){
        if(this.id == null) {
            throw new ApiException(ErrorCode.VACATION_NOT_FOUND);
        } else{
            return this;
        }
    }
}
