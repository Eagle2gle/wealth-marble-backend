package io.eagle.domain.vacation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LatestCahootsListDto {
    List<LatestCahootsDto> result;
}
