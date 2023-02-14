package io.eagle.domain.vacation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class LatestCahootsDto {
    private Long id;
    private String title;

    private LocalDate stockStart;
    private Integer expectedRateOfReturn;

    private List<String> images;
}
