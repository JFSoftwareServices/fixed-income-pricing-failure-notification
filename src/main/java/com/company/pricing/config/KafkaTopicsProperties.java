package com.company.pricing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.kafka")
public record KafkaTopicsProperties(
        String pricingFailure) {
}