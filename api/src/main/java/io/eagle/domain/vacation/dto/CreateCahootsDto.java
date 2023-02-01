package io.eagle.domain.vacation.dto;

import io.eagle.entity.Vacation;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.embeded.Plan;
import io.eagle.entity.embeded.Stock;
import io.eagle.entity.embeded.Theme;
import io.eagle.entity.type.ThemeBuildingType;
import io.eagle.entity.type.ThemeLocationType;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.exception.ApiException;
import io.eagle.exception.error.ErrorCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateCahootsDto {

    @Length(min = 5, max = 15, message = "공모 제목을 5자 이상, 15자 미만으로 작성해주세요.")
    private final String title;

    private final ThemeLocationType themeLocation;

    private final ThemeBuildingType themeBuilding;

    @NotBlank(message = "휴가지 위치를 확인해주세요.")
    private final String location;

    @Positive(message = "휴가지 건설 예상 시간을 확인해주세요.")
    private final Integer expectedMonth;

    @Positive(message = "휴가지 건설 진행 예상 비용을 확인해주세요.")
    @Max(100000000)
    private final Long expectedTotalCost;

    @NotBlank
    @Size(min = 10, max = 50, message = "휴가지 아이디어 간략 소개를 10자이상 50자 이하로 작성해주세요.")
    private final String shortDescription;

    @NotBlank
    @Size(min = 5, max = 4000, message = "휴가지 아이디어 상세 소개를 5자이상 4000자 이하로 작성해주세요.")
    private final String descritption;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate stockStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate stockEnd;

    @Positive(message = "1주당 공모 가격을 작성해주세요.")
    private final Long stockPrice;

    @Positive(message = "목표 공모 주식 수를 작성해주세요.")
    private final Integer stockNum;

    @Positive(message = "예상 수익률을 작성해주세요.")
    private final Integer expectedRateOfReturn;

    // TODO : hashtag

    private final List<MultipartFile> images;

    /*
     * 공모 시작 및 마감 날짜 검증
     * */
    public void validateCahootsPeriod(){
        LocalDate today = LocalDate.now();
        if(
                this.stockStart.compareTo(this.stockEnd) > 0
                        || today.compareTo(this.stockStart) > 0
                        || today.compareTo(this.stockEnd) > 0
        ) {
            throw new ApiException(ErrorCode.VACATION_PERIOD_INVALID);
        }
    }

    public boolean isImagesEmpty(){
        return images == null || images.isEmpty();
    }

    public Vacation buildVacation(){
        return Vacation.builder()
                .status(VacationStatusType.CAHOOTS_BEFORE)
                .title(getTitle())
                .theme(Theme.builder().location(getThemeLocation()).building(getThemeBuilding()).build())
                .location(getLocation())
                .plan(Plan.builder().expectedMonth(getExpectedMonth()).expectedTotalCost(getExpectedTotalCost()).build())
                .shortDescription(getShortDescription())
                .descritption(getDescritption())
                .stockPeriod(Period.builder().start(getStockStart()).end(getStockEnd()).build())
                .stock(Stock.builder().num(getStockNum()).price(getStockPrice()).build())
                .expectedRateOfReturn(getExpectedRateOfReturn())
                .build();
    }
}
