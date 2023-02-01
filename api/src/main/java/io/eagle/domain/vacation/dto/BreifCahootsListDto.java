package io.eagle.domain.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BreifCahootsListDto {
    List<BreifCahootsDto> result;
}
