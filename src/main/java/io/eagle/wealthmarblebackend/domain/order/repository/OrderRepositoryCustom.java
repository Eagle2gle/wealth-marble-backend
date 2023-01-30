package io.eagle.wealthmarblebackend.domain.order.repository;

import io.eagle.wealthmarblebackend.domain.order.entity.Order;
import io.eagle.wealthmarblebackend.domain.user.entity.User;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findAllByUser(User user);

}
