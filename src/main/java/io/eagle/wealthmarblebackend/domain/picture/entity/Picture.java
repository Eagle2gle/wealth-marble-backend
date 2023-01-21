package io.eagle.wealthmarblebackend.domain.picture.entity;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private <User|Vacation> foreignId;
    @ManyToOne
    @JoinColumn(name = "cahoots_id")
    private Vacation vacation;
    private String type;
    private String url;

    @Builder
    public Picture(String url, String type, Vacation vacation ){
        this.url = url;
        this.type = type;
        this.vacation = vacation;
    }
}