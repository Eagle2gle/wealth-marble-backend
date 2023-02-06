package io.eagle.domain.market.consumer;

import io.eagle.common.KafkaConstants;
import io.eagle.domain.market.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketConsumer {

    private final SimpMessagingTemplate template;

    @KafkaListener( topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID )
    public void listen(MessageDto message){ // kafka listener에서 듣고있음
        System.out.println("kafka consumer.. ");
        System.out.println(message);
        template.convertAndSend("/topic/market/" + message.getMarketId(), message); // topic/market/{마켓아이디}를 듣고있는 client에 전송
    }
}
