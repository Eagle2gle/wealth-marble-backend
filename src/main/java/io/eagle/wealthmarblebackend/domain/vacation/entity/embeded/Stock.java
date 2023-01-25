package io.eagle.wealthmarblebackend.domain.vacation.entity.embeded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@Getter
public class Stock {
    @NotNull
    private Long price;
    @NotNull
    private Integer num;

    @Builder
    public Stock(Long price, Integer num) {
        this.price = price;
        this.num = num;
    }
}
