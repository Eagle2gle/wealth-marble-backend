package io.eagle.entity;

import io.eagle.entity.BaseEntity;
import io.eagle.entity.Order;
import lombok.Data;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vacation vacation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
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
