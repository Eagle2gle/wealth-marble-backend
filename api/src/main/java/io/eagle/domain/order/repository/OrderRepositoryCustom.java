package io.eagle.domain.order.repository;

import io.eagle.entity.Order;
import io.eagle.entity.User;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findAllByUser(User user);

}
