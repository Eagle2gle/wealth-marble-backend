package io.eagle.wealthmarblebackend.domain.interest.entity;

import io.eagle.wealthmarblebackend.domain.user.entity.User;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vacation vacation;
}
