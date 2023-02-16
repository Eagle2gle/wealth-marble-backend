package io.eagle.domain.vacation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImminentInfoDto {
    private Long id;
    private String title;
    private Integer competitionRate;
}
