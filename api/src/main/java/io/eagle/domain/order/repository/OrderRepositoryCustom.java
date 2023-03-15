package io.eagle.domain.order.repository;

import io.eagle.domain.order.dto.response.AvailableOrderDto;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderType;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findAllByUser(User user);
    List<AvailableOrderDto> findTop5ByVacationIdAndOrderTypeOrderByPrice(Long id, OrderType type);
}
