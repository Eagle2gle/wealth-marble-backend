package io.eagle.wealthmarblebackend.domain.cahoots.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ContestParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : FK 추가
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cahoots_id")
    private Cahoots cahoots;
}
