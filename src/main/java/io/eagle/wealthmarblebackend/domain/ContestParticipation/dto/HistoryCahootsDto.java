package io.eagle.wealthmarblebackend.domain.ContestParticipation.dto;

import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class HistoryCahootsDto {

    private final LocalDateTime time;

    private final Integer stocks;

    public static HistoryCahootsDto toDto(ContestParticipation c){
        return new HistoryCahootsDto(c.getCreatedAt(), c.getStocks());
    }
}
