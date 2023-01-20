package io.eagle.wealthmarblebackend.domain.cahoots.domain;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.embeded.Period;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.embeded.Plan;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.embeded.Stock;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.embeded.Theme;
import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.VacationStatusType;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.user.domain.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vacation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : FK 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "admin_id")
//    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VacationStatusType status; // CAHOOTS_BEFORE, CAHOOTS_ONGOING, CAHOOTS_OPEN, CAHOOTS_CLOSE

    @NotNull
    @Length(min = 5, max = 15)
    private String title;

    @Embedded
    private Theme theme;

    // TODO : hashtag
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private Set<Hashtag> hashtagList;
    @NotNull
    private String location;

    @Embedded
    private Plan plan;

    @NotNull
    @Length(min = 10, max = 50)
    private String shortDescription;

    @NotNull
    @Length(min = 5, max = 4000)
    private String descritption;

    @Embedded
    private Period stockPeriod;

    @Embedded
    private Stock stock;

    public Vacation(CreateCahootsDto createCahootsDto) {
        this.status = VacationStatusType.CAHOOTS_BEFORE;
        this.title = createCahootsDto.getTitle();
        this.theme = new Theme(createCahootsDto.getThemeLocation(), createCahootsDto.getThemeBuilding());
        this.location = createCahootsDto.getLocation();
        this.plan = new Plan(createCahootsDto.getExpectedMonth(), createCahootsDto.getExpectedTotalCost() * 10000);
        this.shortDescription = createCahootsDto.getShortDescription();
        this.descritption = createCahootsDto.getDescritption();
        this.stockPeriod = new Period(createCahootsDto.getStockStart(), createCahootsDto.getStockEnd());
        this.stock = new Stock(createCahootsDto.getStockPrice(), createCahootsDto.getStockNum());
    }

}