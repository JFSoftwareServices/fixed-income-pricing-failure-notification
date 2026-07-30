package com.company.pricing.messaging.consumer;

import com.company.pricing.domain.PricingFailureEvent;
import com.company.pricing.notification.TraderNotificationService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.*;

import com.company.pricing.domain.AssetClass;
import com.company.pricing.domain.FailureReason;

class SalesTraderNotificationConsumerTest {


    @Test
    void shouldDelegatePricingFailureEventToNotificationService() {

        TraderNotificationService notificationService =
                mock(TraderNotificationService.class);


        SalesTraderNotificationConsumer consumer =
                new SalesTraderNotificationConsumer(notificationService);


        PricingFailureEvent event =
                new PricingFailureEvent(
                        UUID.randomUUID(),
                        "RFQ-10001",
                        "UK GILT",
                        AssetClass.FIXED_INCOME,
                        FailureReason.PRICING_TIMEOUT,
                        Instant.now()
                );


        consumer.receive(event);


        verify(notificationService)
                .notifySalesTrader(event);
    }
}
