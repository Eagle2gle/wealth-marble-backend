package io.eagle.wealthmarblebackend.domain.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BreifCahootListDto {
    List<BreifCahootsDto> result;
}
