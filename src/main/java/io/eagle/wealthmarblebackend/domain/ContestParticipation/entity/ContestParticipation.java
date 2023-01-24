package io.eagle.wealthmarblebackend.domain.ContestParticipation.entity;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContestParticipation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : FK 추가
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name = "cahoots_id")
    private Vacation vacation;

    @Positive
    private Integer stocks;

    @Builder
    public ContestParticipation(Long userId, Vacation vacation, Integer stocks) {
        this.stocks = stocks;
        this.vacation = vacation;
    }
}
