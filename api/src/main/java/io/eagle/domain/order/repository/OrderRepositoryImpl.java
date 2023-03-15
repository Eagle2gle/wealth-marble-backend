package io.eagle.domain.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.order.dto.response.AvailableOrderDto;
import io.eagle.entity.Order;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderType;
import lombok.RequiredArgsConstructor;

import static io.eagle.entity.QOrder.order;
import static io.eagle.entity.type.OrderStatus.ONGOING;
import static io.eagle.entity.type.OrderType.SELL;

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


    public List<AvailableOrderDto> findTop5ByVacationIdAndOrderTypeOrderByPrice(Long id, OrderType type){
        return jpqlQueryFactory
                .select(Projections.fields(AvailableOrderDto.class,
                        order.price,
                        order.amount.sum().as("amount"),
                        order.orderType))
                .from(order)
                .where( order.status.eq(ONGOING))
                .groupBy(order.vacation, order.price)
                .having(order.vacation.id.eq(id), order.orderType.eq(type))
                .orderBy((type == SELL ? order.price.asc() : order.price.desc()))
                .limit(5)
                .fetch();
    }
}
