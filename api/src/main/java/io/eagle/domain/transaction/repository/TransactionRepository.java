package io.eagle.domain.transaction.repository;

import io.eagle.entity.Transaction;
import io.eagle.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {
}
