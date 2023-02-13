package io.eagle.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class KafkaConstants {
    public static final String KAFKA_TOPIC = "kafka-market";
    public static final String GROUP_ID = "eagle";

    @Value("${kafka.broker.address}")
    public static String BROKER;// = "localhost:29092";
    @Value("${kafka.broker.address}")
    public void setBROKER(String broker){
        KafkaConstants.BROKER = broker;
    }

}
