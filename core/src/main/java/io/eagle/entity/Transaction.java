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
    @JoinColumn(name = "vacation_id", insertable = false, updatable = false) // id만으로 repository.save 가능하도록 옵션 설정
    private Vacation vacation;

    @Column(name="vacation_id") // id만으로 repository.save 가능
    private Long vacationId;

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

//    public UserTransactionInfoDto toUserTransactionInfoDto(Long orderId) {
//        String transactionType = this.buyOrder.getId().equals(orderId) ? "BUY" : "SELL";
//        return UserTransactionInfoDto
//            .builder()
//            .vacationName(this.vacation.getTitle())
//            .transactionTime(this.getCreatedAt().format(DateTimeFormatter.ofPattern("YY-MM-DD")).toString())
//            .price(this.price)
//            .amount(this.amount)
//            .transactionType(transactionType)
//            .build();
//    }
}
