package com.company.pricing.messaging.consumer;


import com.company.pricing.domain.PricingFailureEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class FixedIncomeTraderNotificationConsumer {


    @KafkaListener(
            topics = "pricing-failure-events",
            groupId = "fixed-income-trader"
    )
    public void receive(PricingFailureEvent event) {


        System.out.println(
                "Notify Fixed Income Trader for RFQ: "
                        + event.rfqId()
        );

    }
}
