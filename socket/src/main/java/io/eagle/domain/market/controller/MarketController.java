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

    @MessageMapping("/") //   url : "/purchase/"
    public void buy(MessageDto message){
        // 사용자 잔액 조회
        // 구매
        // broadcasting
        System.out.println("producer");
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message);
    }

}
