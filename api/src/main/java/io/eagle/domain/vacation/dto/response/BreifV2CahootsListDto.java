package io.eagle.domain.vacation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BreifV2CahootsListDto {
    List<BreifV2CahootsDto> result;
}