package io.eagle.config;

import com.google.common.collect.ImmutableMap;
import io.eagle.common.KafkaConstants;
import io.eagle.domain.order.dto.response.BroadcastMessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {


    @Bean
    ConcurrentKafkaListenerContainerFactory<String, BroadcastMessageDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BroadcastMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BroadcastMessageDto> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigurations(), new StringDeserializer(), new ErrorHandlingDeserializer(new JsonDeserializer<>(BroadcastMessageDto.class)));
    }

    @Bean
    public Map<String, Object> consumerConfigurations() {
        JsonDeserializer<BroadcastMessageDto> deserializer = new JsonDeserializer<>(BroadcastMessageDto.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return ImmutableMap.<String, Object>builder()
                .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BROKER)
                .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class)
                .put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID)
                .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest")
                .build();
    }
}
