package com.company.pricing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Kafka topics used by the pricing service.
 */
@ConfigurationProperties(prefix = "application.kafka")
public record KafkaTopicsProperties(
        String pricingFailureTopic) {
}