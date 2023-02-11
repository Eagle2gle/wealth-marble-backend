package io.eagle.domain.transaction.repository;

import io.eagle.entity.Transaction;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<Transaction> findByPrice(Integer price);
}
