package io.eagle.domain.market.controller;

import io.eagle.common.KafkaConstants;
import io.eagle.domain.market.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MarketController {
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

//    @MessageMapping("/sale") //   url : "order/sale"
//    public void sale(MessageDto message){
//        BroadcastMessageDto broadcastMessageDto = orderService.sellMarket(message);
//        log.info("[STOMP Producer] user sell market {} price : {}, amount : {}, left : {}",message.getMarketId(),message.getPrice(), message.getAmount());
//        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, broadcastMessageDto);
//    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // '/user/queue/errors' 를 구독하고 있어야함
    public ErrorDto handleException(SocketException exception) {
        log.warn(exception.getMessage());
        ErrorDto errorMessage = ErrorDto.builder().status("fail").message(exception.getMessage()).build();
        return errorMessage;
    }

}
