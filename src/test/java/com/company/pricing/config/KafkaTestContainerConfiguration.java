package com.company.pricing.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class KafkaTestContainerConfiguration {

    @Bean
    public KafkaContainer kafkaContainer() {
        KafkaContainer kafka = new KafkaContainer(
                DockerImageName.parse(
                        "apache/kafka:3.8.0"));
        kafka.start();
        return kafka;
    }
}
