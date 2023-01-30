package io.eagle.wealthmarblebackend.domain.transaction.repository;

import io.eagle.wealthmarblebackend.domain.order.entity.Order;
import io.eagle.wealthmarblebackend.domain.transaction.entity.Transaction;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<Transaction> findAllByOrder(Order order);


}
