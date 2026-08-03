package com.company.pricing.config;

import com.company.pricing.bdd.support.RecordingTraderNotificationServiceSpy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test configuration for Cucumber integration tests.
 *
 * Starts an embedded Kafka broker using Testcontainers and replaces the
 * production TraderNotificationService with a spy implementation so that
 * BDD scenarios can verify trader notification behaviour.
 */
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

     /**
     * Test spy that keeps notification behaviour while recording calls
     * for BDD assertions.
     *
     * @Primary ensures this bean is injected instead of the production
     * TraderNotificationService during tests.
     */
    @Bean
    @Primary
    public RecordingTraderNotificationServiceSpy recordingTraderNotificationService() {
        return new RecordingTraderNotificationServiceSpy();
    }
}
