package io.eagle.domain.ContestParticipation.dto;

import io.eagle.entity.ContestParticipation;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class HistoryCahootsDto {

    private final LocalDateTime time;

    private final Integer stocks;


    public static HistoryCahootsDto toDto(ContestParticipation contestParticipation){
        return new HistoryCahootsDto(contestParticipation.getCreatedAt(), contestParticipation.getStocks());
    }
}
