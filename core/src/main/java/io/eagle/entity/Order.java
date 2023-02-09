package io.eagle.entity;

import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="orders")
@DynamicUpdate
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id", insertable = false, updatable = false) // id만으로 repository.save 가능하도록 옵션 설정
    private Vacation vacation;

    @Column(name="vacation_id") // id만으로 repository.save 가능
    private Long vacationId;

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

    public Order deepCopy(){
        return Order.builder()
                .status(getStatus())
                .vacationId(getVacationId())
                .price(getPrice())
                .user(getUser())
                .orderType(getOrderType())
                .amount(getAmount())
                .build();
    }
}
