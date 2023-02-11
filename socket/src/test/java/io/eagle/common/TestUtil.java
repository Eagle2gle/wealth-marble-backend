package io.eagle.common;

import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.embeded.Plan;
import io.eagle.entity.embeded.Stock;
import io.eagle.entity.embeded.Theme;
import io.eagle.entity.type.*;

import java.time.LocalDate;

public class TestUtil {

    public Vacation createVacation(User user) {
        return Vacation
            .builder()
            .user(user)
            .status(VacationStatusType.CAHOOTS_CLOSE)
            .title("다나카와 함께하는")
            .theme(Theme.builder()
                .building(ThemeBuildingType.GUEST_HOUSE)
                .location(ThemeLocationType.BEACH)
                .build()
            )
            .location("부산")
                .country("대한민국")
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
            .cash(9000000)
            .build();
    }

    public Order createOrder(User user, Vacation vacation, Integer price, Integer amount, OrderType orderType) {
        return Order.builder()
            .user(user)
            .vacation(vacation)
            .orderType(orderType)
            .price(price)
            .amount(amount)
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
}
