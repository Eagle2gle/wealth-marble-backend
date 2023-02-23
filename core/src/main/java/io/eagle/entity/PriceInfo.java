package io.eagle.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PriceInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id")
    private Vacation vacation;

    @Column
    private Integer startPrice;

    @Column
    private Integer highPrice;

    @Column
    private Integer lowPrice;
    @Column
    private Integer standardPrice;
    @Column
    private Integer transactionAmount;
    @Column
    private Integer transactionMoney;

}
