package com.company.pricing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaConfig {

    @Bean
    public NewTopic pricingFailureTopic(KafkaTopicsProperties properties) {
        return TopicBuilder.name(properties.pricingFailure())
                .partitions(1)
                .replicas(1)
                .build();
    }
}