package io.eagle.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class KafkaConstants {
    public static final String KAFKA_TOPIC = "kafka-market";
    public static final String GROUP_ID = "eagle";

    public static String BROKER = "host.docker.internal:29092";
}
