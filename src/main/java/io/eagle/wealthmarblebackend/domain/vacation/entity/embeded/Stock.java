package io.eagle.wealthmarblebackend.domain.cahoots.domain.embeded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Stock {
    @NotNull
    private Long price;
    @NotNull
    private Integer num;
}
