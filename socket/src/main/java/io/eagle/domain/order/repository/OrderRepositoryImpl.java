package io.eagle.domain.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.order.dto.TotalMountDto;
import io.eagle.entity.Order;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import lombok.RequiredArgsConstructor;

import java.util.List;

import java.util.stream.Collectors;

import static io.eagle.entity.QOrder.order;

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

    public List<Order> findAllByVacation(Long vacationId, OrderType type, Integer price){
        return jpqlQueryFactory
                .selectFrom(order)
                .where(order.vacation.id.eq(vacationId),
                        isEqualType(type) ,
                        isEqualPrice(price),
                        isOngoing())
                .orderBy(order.createdAt.asc())
                .fetch();
    }

    public TotalMountDto getCurrentOrderAmount(Long vacationId, Integer price, OrderType type) {
        return jpqlQueryFactory
                .select(Projections.fields(TotalMountDto.class,order.orderType.as("type"), order.amount.sum().as("amount")))
                .from(order)
                .where(order.status.eq(OrderStatus.ONGOING))
                .groupBy(order.orderType, order.vacation.id, order.price)
                .having(order.vacation.id.eq(vacationId), isEqualPrice(price))
                .fetchOne();
    }

    private BooleanExpression isEqualType(OrderType type){
        return type != null ? order.orderType.eq(type) : null;
    }

    private BooleanExpression isEqualPrice(Integer price){
        return price != null ? order.price.eq(price) : null;
    }

    private BooleanExpression isOngoing(){
        return order.status.eq(OrderStatus.ONGOING);
    }
}
