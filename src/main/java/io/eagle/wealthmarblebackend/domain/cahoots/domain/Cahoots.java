package io.eagle.wealthmarblebackend.domain.cahoots.domain;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.ThemeBuildingType;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.ThemeLocationType;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.VacationStatusType;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.user.domain.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cahoots extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : FK 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "admin_id")
//    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VacationStatusType vacationStatusType; // CAHOOTS_BEFORE, CAHOOTS_ONGOING, CAHOOTS_OPEN, CAHOOTS_CLOSE

    @NotNull
    @Length(min = 5, max = 15)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemeLocationType themeLocation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemeBuildingType themeBuilding;

    // TODO : hashtag
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private Set<Hashtag> hashtagList;
    @NotNull
    private String location;

    @NotNull
    private Integer expectedMonth;

    @NotNull
//    @Max(1000000000000)
    private Long expectedTotalCost;

    @NotNull
    @Length(min = 10, max = 50)
    private String shortDescription;

    @NotNull
    @Length(min = 5, max = 4000)
    private String descritption;

    @NotNull
    private LocalDate stockStart;
    @NotNull
    private LocalDate stockEnd;
    @NotNull
    private Long stockPrice;
    @NotNull
    private Integer stockNum;

    public Cahoots(CreateCahootsDto createCahootsDto) {
        this.vacationStatusType = VacationStatusType.CAHOOTS_BEFORE;
        this.title = createCahootsDto.getTitle();
        this.themeLocation = createCahootsDto.getThemeLocation();
        this.themeBuilding = createCahootsDto.getThemeBuilding();
        this.location = createCahootsDto.getLocation();
        this.expectedMonth = createCahootsDto.getExpectedMonth();
        this.expectedTotalCost = createCahootsDto.getExpectedTotalCost() * 10000;
        this.shortDescription = createCahootsDto.getShortDescription();
        this.descritption = createCahootsDto.getDescritption();
        this.stockStart = createCahootsDto.getStockStart();
        this.stockEnd = createCahootsDto.getStockEnd();
        this.stockPrice = createCahootsDto.getStockPrice();
        this.stockNum = createCahootsDto.getStockNum();
    }

}
