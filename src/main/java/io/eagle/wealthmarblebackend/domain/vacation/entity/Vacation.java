package io.eagle.wealthmarblebackend.domain.vacation.entity;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import io.eagle.wealthmarblebackend.domain.picture.entity.Picture;
import io.eagle.wealthmarblebackend.domain.user.domain.User;
import io.eagle.wealthmarblebackend.domain.vacation.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.embeded.Period;
import io.eagle.wealthmarblebackend.domain.vacation.entity.embeded.Plan;
import io.eagle.wealthmarblebackend.domain.vacation.entity.embeded.Stock;
import io.eagle.wealthmarblebackend.domain.vacation.entity.embeded.Theme;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;

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

    @OneToMany(mappedBy = "vacation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Picture> pictureList;

    @OneToMany(mappedBy="vacation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContestParticipation> historyList;

    @Builder
    public Vacation(CreateCahootsDto createCahootsDto) {
        this.status = VacationStatusType.CAHOOTS_BEFORE;
        this.title = createCahootsDto.getTitle();
        this.theme = Theme.builder()
                        .location(createCahootsDto.getThemeLocation())
                        .building(createCahootsDto.getThemeBuilding())
                        .build();
        this.location = createCahootsDto.getLocation();
        this.plan = Plan.builder()
                        .expectedMonth(createCahootsDto.getExpectedMonth())
                        .expectedTotalCost(createCahootsDto.getExpectedTotalCost() * 10000)
                        .build();
        this.shortDescription = createCahootsDto.getShortDescription();
        this.descritption = createCahootsDto.getDescritption();
        this.stockPeriod = Period.builder()
                            .start(createCahootsDto.getStockStart())
                            .end(createCahootsDto.getStockEnd())
                            .build();
        this.stock = Stock.builder()
                        .price(createCahootsDto.getStockPrice())
                        .num(createCahootsDto.getStockNum())
                        .build();
    }

    public void setPictureList(List<Picture> pictureList) {
        for(Picture picture : pictureList) {
            picture.setVacation(this);
        }
        this.pictureList = pictureList;
    }

}