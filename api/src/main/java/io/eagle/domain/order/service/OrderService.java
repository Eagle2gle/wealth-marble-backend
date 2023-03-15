package io.eagle.domain.order.service;

import io.eagle.domain.order.dto.response.AvailableOrderDto;
import io.eagle.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.eagle.entity.type.OrderType.BUY;
import static io.eagle.entity.type.OrderType.SELL;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<AvailableOrderDto> getOrderList(Long vacationId){
        List<AvailableOrderDto> result = orderRepository.findTop5ByVacationIdAndOrderTypeOrderByPrice(vacationId, SELL)
                .stream()
                .sorted(Comparator.comparing(AvailableOrderDto::getPrice).reversed())
                .collect(Collectors.toList());
        List<AvailableOrderDto> result2 = orderRepository.findTop5ByVacationIdAndOrderTypeOrderByPrice(vacationId, BUY);
        result.addAll(result2);
        return result;
    }

}
