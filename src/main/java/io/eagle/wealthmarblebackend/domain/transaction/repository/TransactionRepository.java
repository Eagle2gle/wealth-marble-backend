package io.eagle.wealthmarblebackend.domain.transaction.repository;

import io.eagle.wealthmarblebackend.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {
}
