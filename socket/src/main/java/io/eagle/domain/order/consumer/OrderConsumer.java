package io.eagle.domain.order.consumer;

import io.eagle.common.KafkaConstants;
import io.eagle.domain.order.dto.response.BroadcastStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final SimpMessagingTemplate template;

    @KafkaListener( topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID )
    public void listen(BroadcastStockDto stock){ // kafka listener에서 듣고있음
        template.convertAndSend("/topic/market/" + stock.getMarketId(), stock); // topic/market/{마켓아이디}를 듣고있는 client에 전송
    }
}
