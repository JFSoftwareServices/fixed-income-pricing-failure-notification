package com.company.pricing.messaging.publisher;


import com.company.pricing.config.KafkaTopicsProperties;
import com.company.pricing.domain.PricingFailureEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;


import java.time.Instant;
import java.util.UUID;


import static org.mockito.Mockito.*;


class PricingFailureEventPublisherTest {


    @Test
    void shouldPublishPricingFailureEvent() {


        KafkaTemplate<String, PricingFailureEvent> kafkaTemplate =
                mock(KafkaTemplate.class);


        KafkaTopicsProperties topics =
                new KafkaTopicsProperties(
                        "pricing-failure-events"
                );


        PricingFailureEventPublisher publisher =
                new PricingFailureEventPublisher(
                        kafkaTemplate,
                        topics
                );


        PricingFailureEvent event =
                new PricingFailureEvent(
                        UUID.randomUUID(),
                        "RFQ-10001",
                        "UK GILT",
                        null,
                        null,
                        Instant.now()
                );


        publisher.publish(event);


        verify(kafkaTemplate)
                .send(
                        "pricing-failure-events",
                        "RFQ-10001",
                        event
                );

    }
}