package io.eagle.domain.order.service;

import io.eagle.domain.order.dto.StockVO;
import io.eagle.domain.order.dto.request.StockDto;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.stock.repository.StockRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Order;
import io.eagle.entity.Stock;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import io.eagle.exception.SocketException;
import io.eagle.repository.UserRepository;
import io.eagle.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static io.eagle.entity.type.MarketRankingType.CountryKey;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProduceService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final VacationRepository vacationRepository;
    private final StockRepository stockRepository;
    private final RedisTemplate redisTemplate;
    private ZSetOperations<String, String> redisZSet;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${eagle.score.order}")
    private Integer orderScore;

    @PostConstruct
    public void init(){
        this.redisZSet = redisTemplate.opsForZSet();
    }

    @Transactional
    public StockVO saveMarketOrder(StockDto stockDto, OrderType orderType) {
        StockDto verifiedStock = verifyStock(stockDto, orderType);
        User user = verifyUser(
            userRepository.findById(verifiedStock.getRequesterId()).orElseThrow(() -> new SocketException("존재하지 않는 사용자입니다.")),
            verifiedStock
        );
        Order order = orderRepository.save(
            stockDto.buildOrder(user, vacationRepository.getReferenceById(verifiedStock.getMarketId()), stockDto.getAmount(), OrderStatus.ONGOING)
        );
        incrementInterestMarketScore(verifiedStock);
        return StockVO
            .builder()
            .orderId(order.getId())
            .orderType(order.getOrderType())
            .marketId(order.getVacation().getId())
            .userId(order.getUser().getId())
            .amount(order.getAmount())
            .price(order.getPrice())
            .build();
    }

    private StockDto verifyStock(StockDto stock, OrderType orderType){
        if(stock.getAmount() <= 0) throw new SocketException("요청 수량이 올바르지 않습니다.");
        if(stock.getPrice() <= 0) throw new SocketException("요청 가격이 올바르지 않습니다.");

        try {
            Long userId = jwtTokenProvider.getUserIdFromToken(stock.getToken());
            stock.setRequesterId(userId);
            stock.setOrderType(orderType);
            return stock;
        } catch(Exception e){
            e.printStackTrace();
            throw new SocketException("요청자가 올바르지 않습니다.");
        }
    }

    private User verifyUser(User user, StockDto stock) {
        if (stock.getOrderType().equals(OrderType.BUY)) {
            verifyUserCash(user.getCash(), stock);
            return subtractUserCash(user, stock);
        } else {
            Stock userStock = verifyUserStock(user.getId(), stock.getMarketId(), stock.getAmount());
            subtractUserStock(stock, userStock);
            return user;
        }
    }

    private void verifyUserCash(Long userCash, StockDto stock){
        Integer totalPrice = stock.getPrice() * stock.getAmount();
        if (totalPrice > userCash) { // error handling
            throw new SocketException("사용자 캐시가 부족합니다");
        }
    }

    private Stock verifyUserStock(Long userId, Long vacationId, Integer sellAmount) {
        Stock stock = stockRepository.getUserCurrentStock(userId, vacationId);
        if (stock == null || stock.getAmount() < sellAmount) {
            throw new SocketException("판매할 스톡의 개수가 부족합니다.");
        }
        return stock;
    }

    private User subtractUserCash(User user, StockDto stock){
        Long leftCash = user.getCash() - (stock.getAmount() * stock.getPrice());
        user.setCash(leftCash);
        return userRepository.save(user);
    }

    private void subtractUserStock(StockDto sellStockDto, Stock stock) {
        // 1주당 가격(전체 투자 금액(price) / 전체 개수(amount)) * 판매 수량(sellAmount) <- 빼야할 금액
        Integer subtractPrice = stock.getPrice() * sellStockDto.getAmount() / stock.getAmount();
        stock.setAmount(stock.getAmount() - sellStockDto.getAmount());
        stock.setPrice(stock.getPrice() - subtractPrice);
        stockRepository.save(stock);
    }

    private void incrementInterestMarketScore(StockDto stock){
        String key = CountryKey(vacationRepository.findById(stock.getMarketId()).get().getCountry());
        String value = stock.getMarketId().toString();
        Integer score = stock.getAmount() * orderScore;
        redisZSet.incrementScore(key, value, (double) score);
    }

}
