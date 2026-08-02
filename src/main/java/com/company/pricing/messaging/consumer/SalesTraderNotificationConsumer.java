package com.company.pricing.messaging.consumer;

import com.company.pricing.domain.PricingFailureEvent;
import com.company.pricing.notification.TraderNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SalesTraderNotificationConsumer {

    private final TraderNotificationService notificationService;

    public SalesTraderNotificationConsumer(TraderNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${application.kafka.pricing-failure}",
            groupId = "sales-trader"
    )
    public void receive(PricingFailureEvent event) {
        notificationService.notifySalesTrader(event);
    }
}