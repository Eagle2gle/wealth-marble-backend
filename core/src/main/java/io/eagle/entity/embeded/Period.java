package io.eagle.entity.embeded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@Getter
public class Period {
    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;

    @Builder
    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }
}