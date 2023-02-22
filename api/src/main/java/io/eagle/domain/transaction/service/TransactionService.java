package io.eagle.domain.transaction.service;

import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.dto.TransactionRequestDto;
import io.eagle.domain.transaction.dto.TransactionResponseDto;
import io.eagle.domain.transaction.dto.UserTransactionInfoDto;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public List<TransactionResponseDto> getTransactions(TransactionRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(requestDto.getPage(), 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return transactionRepository.findByVacationOrderByCreatedAtDesc(pageRequest, requestDto);
    }

    public List<UserTransactionInfoDto> getMineTransaction(User user) {
        return orderRepository
            .findAllByUser(user)
            .stream()
            .map(order -> transactionRepository.findAllByOrder(order).stream()
                .map(transaction -> {
                    String transactionType = transaction.getBuyOrder().getId().equals(order.getId()) ? "BUY" : "SELL";
                    return UserTransactionInfoDto
                        .builder()
                        .vacationName(transaction.getVacation().getTitle())
                        .transactionTime(transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("YY-MM-DD")).toString())
                        .price(transaction.getPrice())
                        .amount(transaction.getAmount())
                        .transactionType(transactionType)
                        .build();
                }).collect(Collectors.toList())
            ).flatMap(List::stream).collect(Collectors.toList());
    }

}
