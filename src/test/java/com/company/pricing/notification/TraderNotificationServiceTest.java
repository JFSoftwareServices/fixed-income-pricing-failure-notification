package com.company.pricing.notification;

import com.company.pricing.domain.PricingFailureEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

class TraderNotificationServiceTest {

    @Test
    void shouldNotifySalesTrader() {

        TraderNotificationService service = new TraderNotificationService();

        PricingFailureEvent event = new PricingFailureEvent(
                UUID.randomUUID(),
                "RFQ-10001",
                "UK GILT",
                null,
                null,
                Instant.now());

        service.notifySalesTrader(event);

    }

    @Test
    void shouldNotifyFixedIncomeTrader() {

        TraderNotificationService service = new TraderNotificationService();

        PricingFailureEvent event = new PricingFailureEvent(
                UUID.randomUUID(),
                "RFQ-10001",
                "UK GILT",
                null,
                null,
                Instant.now());

        service.notifyFixedIncomeTrader(event);
    }
}
