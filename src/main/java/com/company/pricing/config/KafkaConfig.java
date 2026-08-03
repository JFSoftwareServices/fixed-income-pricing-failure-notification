package com.company.pricing.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration for application-managed topics.
 *
 * Defines topics required by the pricing workflow and provisions them
 * during application startup when Kafka admin is enabled.
 */
@Configuration
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaConfig {

    @Bean
    public NewTopic pricingFailureTopic(KafkaTopicsProperties properties) {
        return TopicBuilder
                .name(properties.pricingFailureTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }
}