package io.eagle.domain.order.controller;

import io.eagle.common.KafkaConstants;
import io.eagle.domain.order.dto.response.ResponseDto;
import io.eagle.domain.order.dto.request.StockDto;
import io.eagle.domain.order.dto.StockVO;
import io.eagle.domain.order.service.OrderProduceService;
import io.eagle.entity.type.OrderType;
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

    private final KafkaTemplate<String, StockVO> kafkaTemplate;
    private final OrderProduceService orderProduceService;

    @MessageMapping("/purchase") //   url : "order/purchase" 로 들어오는 정보 처리
    @SendToUser("/queue/success")
    public ResponseDto purchase(StockDto stockDto){
        StockVO stockVO = orderProduceService.saveMarketOrder(stockDto, OrderType.BUY);
        log.info("[STOMP Producer] user purchase market {} price : {}, amount : {}", stockDto.getMarketId(), stockDto.getPrice(), stockDto.getAmount());
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, stockVO);
        ResponseDto successDto = ResponseDto.builder().status("success").message("주문 성공").build();
        return successDto;
    }

    @MessageMapping("/sale") //   url : "order/sale" 로 들어오는 정보 처리
    public void sale(StockDto stock){
        StockVO stockVO = orderProduceService.saveMarketOrder(stock, OrderType.SELL);
        log.info("[STOMP Producer] user sell market {} price : {}, amount : {}, left : {}", stock.getMarketId(), stock.getPrice(), stock.getAmount());
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, stockVO);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // '/user/queue/errors' 를 구독하고 있어야함
    public ResponseDto handleException(SocketException exception) {
        log.warn(exception.getMessage());
        ResponseDto errorMessage = ResponseDto.builder().status("fail").message(exception.getMessage()).build();
        return errorMessage;
    }

}
