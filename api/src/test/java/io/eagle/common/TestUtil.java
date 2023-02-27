package io.eagle.common;

import io.eagle.entity.*;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.embeded.Plan;
import io.eagle.entity.embeded.Stock;
import io.eagle.entity.embeded.Theme;
import io.eagle.entity.type.*;

import java.time.LocalDate;
import java.util.List;

public class TestUtil {

    public Vacation createVacation(User user) {
        return Vacation
            .builder()
            .id(1L)
            .user(user)
            .status(VacationStatusType.CAHOOTS_CLOSE)
            .title("다나카와 함께하는")
            .theme(Theme.builder()
                .building(ThemeBuildingType.GUEST_HOUSE)
                .location(ThemeLocationType.BEACH)
                .build()
            )
            .country("대한민국")
            .location("부산")
            .plan(Plan.builder()
                .expectedMonth(12)
                .expectedTotalCost(30000L)
                .build()
            )
            .shortDescription("우리 휴양지에 놀러와요")
            .description("우리 휴양지에 놀러와요")
            .stockPeriod(Period.builder()
                .start(LocalDate.now().minusDays(1L))
                .end(LocalDate.now())
                .build()
            )
            .stock(Stock.builder()
                .price(10000L)
                .num(10)
                .build()
            )
            .expectedRateOfReturn(12)
            .pictureList(List.of(new Picture("url", "type", null)))
            .build();
    }

    public User createUser(String name, String email) {
        return User.builder()
            .nickname(name)
            .email(email)
            .providerType(ProviderType.GOOGLE)
            .providerId("12354126646")
            .ranks(Ranks.NAMJAK)
            .role(Role.USER)
            .cash(100)
            .build();
    }

    public Order createOrder(User user, Vacation vacation, OrderType orderType) {
        return Order.builder()
            .user(user)
            .vacation(vacation)
            .orderType(orderType)
            .price(100)
            .amount(10)
            .status(OrderStatus.ONGOING)
            .build();
    }

    public Transaction createTransaction(Vacation vacation, Order buyOrder, Order sellOrder) {
        return Transaction.builder()
            .vacation(vacation)
            .buyOrder(buyOrder)
            .sellOrder(sellOrder)
            .price(100)
            .amount(100)
            .build();
    }

    public Interest createInterest(User user, Vacation vacation) {
        return Interest.builder()
            .user(user)
            .vacation(vacation)
            .build();
    }

    public ContestParticipation createContestParticipation(User user, Vacation vacation) {
        return ContestParticipation.builder()
            .user(user)
            .vacation(vacation)
            .stocks(10)
            .build();
    }

    public io.eagle.entity.Stock createStock(User user, Vacation vacation) {
        return io.eagle.entity.Stock
            .builder()
            .user(user)
            .vacation(vacation)
            .amount(1)
            .price(100)
            .build();
    }

    public PriceInfo createPriceInfo(Vacation vacation) {
        return PriceInfo.builder()
            .vacation(vacation)
            .transactionAmount(10)
            .lowPrice(100)
            .highPrice(1000)
            .standardPrice(500)
            .startPrice(300)
            .transactionMoney(10000)
            .build();
    }
}
