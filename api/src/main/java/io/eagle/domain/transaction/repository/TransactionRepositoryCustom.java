package io.eagle.domain.transaction.repository;

import io.eagle.entity.Order;
import io.eagle.entity.Transaction;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<Transaction> findAllByOrder(Order order);
    Transaction findByVacation(Long vacationId);

}
