package io.eagle.entity;

import io.eagle.entity.BaseEntity;
import io.eagle.entity.ContestParticipation;
import io.eagle.entity.Picture;
//import io.eagle.domain.vacation.dto.CreateCahootsDto;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.embeded.Plan;
import io.eagle.entity.embeded.Stock;
import io.eagle.entity.embeded.Theme;
import io.eagle.entity.type.VacationStatusType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vacation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    @NotNull
    private Integer expectedRateOfReturn;

    public void setPictureList(List<Picture> pictureList) {
        for(Picture picture : pictureList) {
            picture.setVacation(this);
        }
        this.pictureList = pictureList;
    }

}