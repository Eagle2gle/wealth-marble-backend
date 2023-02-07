package io.eagle.domain.transaction.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static io.eagle.entity.QTransaction.transaction;


@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom{

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<Transaction> findAllByOrder(Order order) {
        return jpqlQueryFactory
            .selectFrom(transaction)
            .where(transaction.buyOrder.id.eq(order.getId())
                .or(transaction.sellOrder.id.eq(order.getId())))
            .fetchAll()
            .stream()
            .collect(Collectors.toList());
    }

    @Override
    public Transaction findByVacation(Long vacationId) {
        return jpqlQueryFactory
            .selectFrom(transaction)
            .where(transaction.vacation.id.eq(vacationId))
            .orderBy(transaction.createdAt.desc())
            .fetchOne();
    }

}
