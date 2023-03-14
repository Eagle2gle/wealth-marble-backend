package io.eagle.entity;

import io.eagle.entity.type.ContestParticipationStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContestParticipation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cahoots_id")
    private Vacation vacation;

    @Positive
    private Integer stocks;

    @Enumerated(EnumType.STRING)
    @Column
    private ContestParticipationStatus status = ContestParticipationStatus.PARTICIPATION;

    @Builder
    public ContestParticipation(User user, Vacation vacation, Integer stocks) {
        this.user = user;
        this.stocks = stocks;
        this.vacation = vacation;
    }
}
