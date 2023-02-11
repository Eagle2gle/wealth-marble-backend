package io.eagle.config;

import com.google.common.collect.ImmutableMap;
import io.eagle.common.KafkaConstants;
import io.eagle.domain.order.dto.response.BroadcastMessageDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, BroadcastMessageDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfiguration());
    }

    @Bean
    public Map<String, Object> kafkaProducerConfiguration() {
        return ImmutableMap.<String, Object>builder()
                .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BROKER)
                .put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
//                .put("group.id", KafkaConstants.GROUP_ID)
                .build();
    }

    @Bean
    public KafkaTemplate<String, BroadcastMessageDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
