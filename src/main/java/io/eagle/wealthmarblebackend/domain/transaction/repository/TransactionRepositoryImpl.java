package io.eagle.wealthmarblebackend.domain.transaction.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.wealthmarblebackend.domain.order.entity.Order;
import io.eagle.wealthmarblebackend.domain.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static io.eagle.wealthmarblebackend.domain.transaction.entity.QTransaction.transaction;

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

}
