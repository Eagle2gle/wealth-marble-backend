package io.eagle.wealthmarblebackend.domain.vacation.dto;

import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class BreifCahootsDto {
    private Long id;
    private String title;
    private String location;
    private VacationStatusType status;

    private LocalDate stockStart;

    private LocalDate stockEnd;

    private Long stockPrice;

    private Integer stockNum;

    private Integer competitionRate;

    // TODO : hashtag

    // TODO : picture url
    private List<String> images;

}
