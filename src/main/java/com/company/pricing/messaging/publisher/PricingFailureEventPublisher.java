package com.company.pricing.messaging.publisher;

import com.company.pricing.config.KafkaTopicsProperties;
import com.company.pricing.domain.PricingFailureEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes pricing failure events to the configured Kafka topic.
 */
@Component
public class PricingFailureEventPublisher {

    private final KafkaTemplate<String, PricingFailureEvent> kafkaTemplate;

    private final KafkaTopicsProperties topics;

    public PricingFailureEventPublisher(
            KafkaTemplate<String, PricingFailureEvent> kafkaTemplate,
            KafkaTopicsProperties topics) {

        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    public void publish(PricingFailureEvent event) {

        kafkaTemplate.send(
                topics.pricingFailureTopic(),
                event.rfqId(), // Ensures events for the same RFQ are consistently partitioned.
                event);
    }
}