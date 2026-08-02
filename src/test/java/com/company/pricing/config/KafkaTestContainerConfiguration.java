package com.company.pricing.config;

import com.company.pricing.bdd.support.RecordingTraderNotificationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class KafkaTestContainerConfiguration {

    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    static {
        KAFKA_CONTAINER.start();
    }

    @Bean
    public KafkaContainer kafkaContainer() {
        return KAFKA_CONTAINER;
    }

    @Bean
    @Primary
    public RecordingTraderNotificationService recordingTraderNotificationService() {
        return new RecordingTraderNotificationService();
    }
}
