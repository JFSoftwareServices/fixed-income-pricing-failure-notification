package com.company.pricing.messaging.consumer;

import com.company.pricing.domain.PricingFailureEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;


class FixedIncomeTraderNotificationConsumerTest {


    @Test
    void shouldReceivePricingFailureEvent() {


        FixedIncomeTraderNotificationConsumer consumer =
                new FixedIncomeTraderNotificationConsumer();


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
