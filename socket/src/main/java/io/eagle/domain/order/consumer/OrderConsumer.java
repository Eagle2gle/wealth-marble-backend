package io.eagle.domain.order.consumer;

import io.eagle.common.KafkaConstants;
import io.eagle.domain.order.dto.StockVO;
import io.eagle.domain.order.dto.response.BroadCastOrderDto;
import io.eagle.domain.order.service.OrderConsumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final SimpMessagingTemplate template;
    private final OrderConsumeService orderConsumeService;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void listen(StockVO stockVO) {
        BroadCastOrderDto broadCastOrderDto = orderConsumeService.createTransactionsByOrder(stockVO.getMarketId(), stockVO);
        template.convertAndSend("/topic/market/" + stockVO.getMarketId(), broadCastOrderDto);
    }
}
