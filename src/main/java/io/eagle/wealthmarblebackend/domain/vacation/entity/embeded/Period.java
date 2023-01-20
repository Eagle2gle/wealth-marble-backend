package io.eagle.wealthmarblebackend.domain.cahoots.domain.embeded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Period {
    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}