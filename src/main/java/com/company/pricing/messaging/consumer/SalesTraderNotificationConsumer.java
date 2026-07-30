package com.company.pricing.messaging.consumer;

import com.company.pricing.domain.PricingFailureEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class SalesTraderNotificationConsumer {


    @KafkaListener(
            topics = "pricing-failure-events",
            groupId = "sales-trader"
    )
    public void receive(PricingFailureEvent event) {

        System.out.println(
                "Notify Sales Trader for RFQ: "
                        + event.rfqId()
        );

    }
}
