package io.eagle.wealthmarblebackend.domain.order.repository;

import io.eagle.wealthmarblebackend.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
