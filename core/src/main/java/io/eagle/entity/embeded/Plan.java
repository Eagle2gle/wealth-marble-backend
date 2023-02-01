package io.eagle.entity.embeded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@Getter
public class Plan {
    @NotNull
    private Integer expectedMonth;

    @NotNull
    private Long expectedTotalCost;

    @Builder
    public Plan(Integer expectedMonth, Long expectedTotalCost){
        this.expectedMonth = expectedMonth;
        this.expectedTotalCost = expectedTotalCost;
    }
}
