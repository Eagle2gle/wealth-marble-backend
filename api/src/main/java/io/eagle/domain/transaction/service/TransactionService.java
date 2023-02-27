package io.eagle.domain.transaction.service;

import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.dto.request.RecentTransactionRequestDto;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.dto.response.RecentTransactionResponseDto;
import io.eagle.domain.transaction.dto.response.TransactionResponseDto;
import io.eagle.domain.transaction.dto.response.UserTransactionInfoDto;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Picture;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final VacationRepository vacationRepository;
    private final PictureRepository pictureRepository;
    private final PriceInfoRepository priceInfoRepository;

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

        try {
            sseEmitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sseEmitter.onTimeout(() -> CLIENTS.remove(randomId));
        sseEmitter.onCompletion(() -> CLIENTS.remove(randomId));
        return sseEmitter;
    }

    public void publishRecentTransaction(RecentTransactionRequestDto request) {
        Set<String> deadRandomIds = new HashSet<>();
        RecentTransactionResponseDto response = this.createRecentTransactionResponseDto(request);
        CLIENTS.forEach((randomId, emitter) -> {
            try {
                emitter.send(response);
            } catch (Exception e) {
                deadRandomIds.add(randomId);
            }
        });
        deadRandomIds.forEach(CLIENTS::remove);
    }

    public RecentTransactionResponseDto createRecentTransactionResponseDto(RecentTransactionRequestDto request) {
        Long vacationId = request.getVacationId();
        Vacation vacation = vacationRepository.findById(vacationId).orElse(null);
        try {
            List<String> pictureUrls = pictureRepository.findUrlsByCahootsId(vacationId);
            PriceInfo priceInfo = priceInfoRepository.findOneByVacationId(vacationId);
            Integer standardPrice = priceInfo == null ? vacation.getStock().getPrice().intValue() : priceInfo.getStandardPrice().intValue();
            Integer gap = request.getCurrentPrice() - standardPrice;
            Double gapRate = gap.doubleValue() * 100 / standardPrice;
            return RecentTransactionResponseDto.builder()
                .pictureUrl(pictureUrls != null && pictureUrls.size() > 0 ? pictureUrls.get(0) : null)
                .title(vacation.getTitle())
                .currentPrice(request.getCurrentPrice())
                .gap(request.getCurrentPrice() - standardPrice)
                .gapRate(gapRate)
                .dividend(request.getCurrentPrice() * 0.1)
                .dividendRate(0.1)
                .createdAt(request.getCreatedAt())
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
