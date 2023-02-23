package io.eagle.domain.stock.service;

import io.eagle.domain.stock.dto.response.StockMineDto;
import io.eagle.domain.stock.repository.StockRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final VacationRepository vacationRepository;

    public List<StockMineDto> getMineStock(User user) {
        return stockRepository.getMineStockByUser(user.getId());
    }

}
