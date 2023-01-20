package io.eagle.wealthmarblebackend.domain.ContestParticipation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class HistoryCahootsDtoList {

    List<HistoryCahootsDto> result;
}
