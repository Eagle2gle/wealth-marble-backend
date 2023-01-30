package io.eagle.wealthmarblebackend.domain.order.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.order.entity.Order;
import io.eagle.wealthmarblebackend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import static io.eagle.wealthmarblebackend.domain.order.entity.QOrder.order;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<Order> findAllByUser(User user) {
        return jpqlQueryFactory
            .selectFrom(order)
            .where(order.user.id.eq(user.getId()))
            .fetchAll()
            .stream()
            .collect(Collectors.toList());
    }
}
