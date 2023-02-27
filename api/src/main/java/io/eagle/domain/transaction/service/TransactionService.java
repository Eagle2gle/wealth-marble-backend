package io.eagle.domain.transaction.service;

import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.dto.response.RecentTransactionDto;
import io.eagle.domain.transaction.dto.response.TransactionResponseDto;
import io.eagle.domain.transaction.dto.response.UserTransactionInfoDto;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();

    public List<TransactionResponseDto> getTransactions(TransactionRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(requestDto.getPage(), 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return transactionRepository.findByVacationOrderByCreatedAtDesc(pageRequest, requestDto);
    }

    public List<UserTransactionInfoDto> getMineTransaction(User user) {
        return orderRepository
            .findAllByUser(user)
            .stream()
            .map(order -> transactionRepository.findAllByOrder(order).stream()
                .map(transaction -> new UserTransactionInfoDto(transaction, order.getId())).collect(Collectors.toList())
            ).flatMap(List::stream).collect(Collectors.toList());
    }

    public SseEmitter subscribeRecentTransaction(String randomId) {
        SseEmitter sseEmitter = new SseEmitter();
        CLIENTS.put(randomId, sseEmitter);

        sseEmitter.onTimeout(() -> CLIENTS.remove(randomId));
        sseEmitter.onCompletion(() -> CLIENTS.remove(randomId));
        return sseEmitter;
    }

}
