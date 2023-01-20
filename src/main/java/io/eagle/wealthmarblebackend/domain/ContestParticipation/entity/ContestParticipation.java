package io.eagle.wealthmarblebackend.domain.ContestParticipation.entity;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Data
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
    private Integer amount;
}
