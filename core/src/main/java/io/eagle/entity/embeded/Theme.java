package io.eagle.entity.embeded;

import io.eagle.entity.type.ThemeBuildingType;
import io.eagle.entity.type.ThemeLocationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@Getter
public class Theme {
    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemeLocationType themeLocation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemeBuildingType themeBuilding;

    @Builder
    public Theme(ThemeLocationType location, ThemeBuildingType building){
        this.themeLocation = location;
        this.themeBuilding = building;
    }
}
