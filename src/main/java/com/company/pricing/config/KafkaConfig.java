package com.company.pricing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaConfig {
}