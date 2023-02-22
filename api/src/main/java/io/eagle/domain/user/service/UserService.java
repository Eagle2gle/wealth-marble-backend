package io.eagle.domain.user.service;

import io.eagle.domain.stock.repository.StockRepository;
import io.eagle.domain.user.dto.response.UserInfoDto;
import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public UserInfoDto getUserInfo(User user) {
        Integer value = stockRepository.getTotalStockValueByUser(user.getId()).stream().mapToInt(stock -> stock.getAmount() * stock.getPrice()).sum();
        return new UserInfoDto(value, user);
    }

}
