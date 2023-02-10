package io.eagle.domain.order.repository;

import com.querydsl.core.Tuple;
import io.eagle.domain.order.dto.TotalMountDto;
import io.eagle.entity.Order;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderType;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

    List<Order> findAllByUser(User user);

    List<Order> findAllByVacation(Long vacationId, OrderType type, Integer price);

    TotalMountDto getCurrentOrderAmount(Long vacationId, Integer price, OrderType type);
}
