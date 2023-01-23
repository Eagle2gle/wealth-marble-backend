package io.eagle.wealthmarblebackend.domain.vacation.dto;

import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.ThemeBuildingType;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.ThemeLocationType;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DetailCahootsDto {

    private final Long id;
    private final String title;

    private final ThemeLocationType themeLocation;

    private final ThemeBuildingType themeBuilding;

    private final String location;

    private final Integer expectedMonth;

    private final Long expectedTotalCost;

    @NotBlank
    private final String shortDescription;

    @NotBlank
    private final String descritption;

    private final VacationStatusType status;

    private final LocalDate stockStart;

    private final LocalDate stockEnd;

    private final Long stockPrice;

    private final Integer competitionRate;

    // TODO : hashtag

    // TODO : picture url
    private final List<String> images = new ArrayList<>();

    public static DetailCahootsDto toDto(Vacation vacation, Integer competitionRate) {
        return DetailCahootsDto.builder()
                .id(vacation.getId())
                .title(vacation.getTitle())
                .descritption(vacation.getDescritption())
                .location(vacation.getLocation())
                .expectedMonth(vacation.getPlan().getExpectedMonth())
                .expectedTotalCost(vacation.getPlan().getExpectedTotalCost())
                .shortDescription(vacation.getShortDescription())
                .stockStart(vacation.getStockPeriod().getStart())
                .stockEnd(vacation.getStockPeriod().getEnd())
                .stockPrice(vacation.getStock().getPrice())
                .competitionRate(competitionRate)
                .themeLocation(vacation.getTheme().getThemeLocation())
                .themeBuilding(vacation.getTheme().getThemeBuilding())
                .status(vacation.getStatus())
                .build();

    }
}
