package io.eagle.domain.transaction.repository;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import io.eagle.domain.transaction.dto.response.QTransactionResponseDto;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.dto.response.TransactionResponseDto;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        System.out.println(vacationId);
        return jpqlQueryFactory
            .selectFrom(transaction)
            .where(transaction.vacation.id.eq(vacationId))
            .orderBy(transaction.createdAt.desc())
            .fetchFirst();
    }

    @Override
    public List<TransactionResponseDto> findByVacationOrderByCreatedAtDesc(Pageable page, TransactionRequestDto requestDto) {
        DateTemplate<LocalDateTime> formattedDate = Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", transaction.createdAt, "%Y-%m-%d");
        DateTemplate<LocalDateTime> beforeDate = convertIntoDateTemplate(requestDto.getStartDate());
        DateTemplate<LocalDateTime> afterDate = convertIntoDateTemplate(requestDto.getEndDate());
        return jpqlQueryFactory
            .select(new QTransactionResponseDto(transaction.createdAt, transaction.price, transaction.amount))
            .from(transaction)
            .where(
                transaction.vacation.id.eq(requestDto.getVacationId())
                    .and(formattedDate.between(beforeDate, afterDate))
            )
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .orderBy(transaction.createdAt.desc())
            .fetch();
    }

    private DateTemplate<LocalDateTime> convertIntoDateTemplate(LocalDate date) {
        return Expressions.dateTemplate(LocalDateTime.class, "DATE_FORMAT({0}, {1})", date, "%Y-%m-%d");
    }

}
