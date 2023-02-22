package io.eagle.domain.user.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.stock.dto.StockInfoDto;
import io.eagle.domain.stock.repository.StockRepository;
import io.eagle.domain.user.dto.response.UserInfoDto;
import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("내_정보_가져오기")
    public void getUserInfo() {
        // given
        TestUtil testUtil = new TestUtil();
        User user = testUtil.createUser("me", "me@email.com");
        StockInfoDto stockInfoDto = new StockInfoDto(1000, 1000);

        // when
        when(stockRepository.getTotalStockValueByUser(user.getId())).thenReturn(List.of(stockInfoDto));
        UserInfoDto userInfoDto = userService.getUserInfo(user);

        // then
        assertEquals(userInfoDto.getValue(), 1000000);
        assertEquals(userInfoDto.getUsername(), user.getNickname());
        assertEquals(userInfoDto.getEmail(), user.getEmail());
    }

}
