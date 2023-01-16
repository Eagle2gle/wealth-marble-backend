package io.eagle.wealthmarblebackend.domain.cahoots.domain;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class CahootsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : FK 추가
    private Integer adminId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VacationStatusType VacationStatusType; // CAHOOTS_BEFORE, CAHOOTS_ONGOING, CAHOOTS_OPEN, CAHOOTS_CLOSE

    @NotNull
    @Length(min = 5, max = 15)
    private String title;

    @NotNull
    private String themeLocation;

    @NotNull
    private String themeBuilding;

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

}
