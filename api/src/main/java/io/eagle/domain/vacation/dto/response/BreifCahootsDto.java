package io.eagle.domain.vacation.dto.response;

import io.eagle.entity.type.VacationStatusType;
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

    private List<String> images;

    private Boolean isInterest;

}
