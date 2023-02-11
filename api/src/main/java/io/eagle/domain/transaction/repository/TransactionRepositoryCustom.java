package io.eagle.domain.transaction.repository;

import io.eagle.domain.transaction.dto.TransactionRequestDto;
import io.eagle.domain.transaction.dto.TransactionResponseDto;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<Transaction> findAllByOrder(Order order);
    Transaction findByVacation(Long vacationId);
    List<TransactionResponseDto> findByVacationOrderByCreatedAtDesc(Pageable pageable, TransactionRequestDto requestDto);

}
