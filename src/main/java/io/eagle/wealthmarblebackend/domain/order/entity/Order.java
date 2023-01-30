package io.eagle.wealthmarblebackend.domain.order.entity;

import io.eagle.wealthmarblebackend.common.BaseEntity;
import io.eagle.wealthmarblebackend.domain.order.entity.type.OrderStatus;
import io.eagle.wealthmarblebackend.domain.order.entity.type.OrderType;
import io.eagle.wealthmarblebackend.domain.user.entity.User;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vacation vacation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

}
