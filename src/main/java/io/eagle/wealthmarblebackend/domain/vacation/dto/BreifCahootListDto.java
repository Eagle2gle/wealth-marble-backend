package io.eagle.wealthmarblebackend.domain.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BreifCahootListDto {
    List<BreifCahootsDto> result;
}
