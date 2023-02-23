package io.eagle.domain.stock.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.stock.dto.StockInfoDto;
import io.eagle.domain.stock.dto.response.StockMineDto;
import io.eagle.domain.stock.repository.StockRepository;
import io.eagle.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    @DisplayName("내_자산현황_조회")
    void getMineStockByUser() {
        // given
        TestUtil testUtil = new TestUtil();
        User user = testUtil.createUser("stock", "stock@email.com");
        StockMineDto stockMineDto = StockMineDto.builder()
            .profitRate(1.1)
            .totalAmount(100)
            .currentPrice(100)
            .title("훈이네")
            .pricePerStock(100.0)
            .build();

        // when
        when(stockRepository.getMineStockByUser(user.getId())).thenReturn(List.of(stockMineDto));
        List<StockMineDto> stockMineDtos = stockService.getMineStock(user);

        // then
        StockMineDto result = stockMineDtos.get(0);
        assertEquals(result.getCurrentPrice(), stockMineDto.getCurrentPrice());
        assertEquals(result.getPricePerStock(), stockMineDto.getPricePerStock());
    }

}
