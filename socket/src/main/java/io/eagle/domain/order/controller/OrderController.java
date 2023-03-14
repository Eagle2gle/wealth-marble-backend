package io.eagle.domain.order.controller;

import io.eagle.common.KafkaConstants;
import io.eagle.domain.order.dto.response.ErrorDto;
import io.eagle.domain.order.dto.request.StockDto;
import io.eagle.domain.order.dto.response.BroadcastStockDto;
import io.eagle.domain.order.service.OrderService;
import io.eagle.exception.SocketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final KafkaTemplate<String, BroadcastStockDto> kafkaTemplate;
    private final OrderService orderService;

    @MessageMapping("/purchase") //   url : "order/purchase" 로 들어오는 정보 처리
    public void purchase(StockDto stockDto){
        BroadcastStockDto broadcastMessageDto = orderService.purchaseMarket(stockDto);
        log.info("[STOMP Producer] user purchase market {} price : {}, amount : {}, left : {}", stockDto.getMarketId(), stockDto.getPrice(), stockDto.getAmount(), broadcastMessageDto.getAmount());
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, broadcastMessageDto);
    }

    @MessageMapping("/sale") //   url : "order/sale"
    public void sale(StockDto message){
        BroadcastStockDto broadcastMessageDto = orderService.sellMarket(message);
        log.info("[STOMP Producer] user sell market {} price : {}, amount : {}, left : {}",message.getMarketId(),message.getPrice(), message.getAmount());
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, broadcastMessageDto);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // '/user/queue/errors' 를 구독하고 있어야함
    public ErrorDto handleException(SocketException exception) {
        log.warn(exception.getMessage());
        ErrorDto errorMessage = ErrorDto.builder().status("fail").message(exception.getMessage()).build();
        return errorMessage;
    }

}
