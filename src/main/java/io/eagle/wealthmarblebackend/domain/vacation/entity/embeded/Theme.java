package io.eagle.wealthmarblebackend.domain.vacation.entity.embeded;

import io.eagle.wealthmarblebackend.domain.vacation.entity.type.ThemeBuildingType;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.ThemeLocationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Theme {
    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemeLocationType themeLocation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemeBuildingType themeBuilding;
}
