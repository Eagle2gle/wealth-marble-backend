package io.eagle.wealthmarblebackend.domain.ContestParticipation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
@Builder
public class HistoryCahootsDtoList {

    List<HistoryCahootsDto> result;
}
