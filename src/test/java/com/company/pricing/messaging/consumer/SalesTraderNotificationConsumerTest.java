package com.company.pricing.messaging.consumer;

import com.company.pricing.domain.PricingFailureEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;


class SalesTraderNotificationConsumerTest {


    @Test
    void shouldReceivePricingFailureEvent() {


        SalesTraderNotificationConsumer consumer =
                new SalesTraderNotificationConsumer();


        PricingFailureEvent event =
                new PricingFailureEvent(
                        UUID.randomUUID(),
                        "RFQ-10001",
                        "UK GILT",
                        null,
                        null,
                        Instant.now()
                );


        consumer.receive(event);

    }
}
