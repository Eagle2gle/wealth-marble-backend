package io.eagle.wealthmarblebackend.domain.cahoots.dto;

import io.eagle.wealthmarblebackend.domain.cahoots.domain.Vacation;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.ThemeBuildingType;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.ThemeLocationType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DetailCahootsDto {

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

    private final LocalDate stockStart;

    private final LocalDate stockEnd;

    private final Long stockPrice;

    private final Integer competitionRate;

    // TODO : hashtag

    // TODO : picture url
    private final List<String> images = new ArrayList<>();

    public static DetailCahootsDto toDto(Vacation vacation, Integer competitionRate) {
        return DetailCahootsDto.builder()
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
                .build();

    }
}
