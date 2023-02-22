package io.eagle.entity;

import io.eagle.entity.BaseEntity;
import io.eagle.entity.Order;
import lombok.*;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id")
    private Vacation vacation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_order_id")
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_order_id")
    private Order sellOrder;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer amount;

}
