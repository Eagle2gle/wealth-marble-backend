package io.eagle.wealthmarblebackend.domain.vacation.dto;

import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BreifV2CahootsDto {
    private Long id;
    private String title;
    private VacationStatusType status;

    private LocalDate stockStart;

    private LocalDate stockEnd;

    // TODO : picture url
//    private final List<String> images = new ArrayList<>();

}
