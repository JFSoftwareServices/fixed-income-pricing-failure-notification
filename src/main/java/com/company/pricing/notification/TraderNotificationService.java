package com.company.pricing.notification;

import com.company.pricing.domain.PricingFailureEvent;
import org.springframework.stereotype.Service;


@Service
public class TraderNotificationService {


    public void notifySalesTrader(
            PricingFailureEvent event) {


        System.out.println(
                "Notify Sales Trader. RFQ: "
                        + event.rfqId()
        );

    }


    public void notifyFixedIncomeTrader(
            PricingFailureEvent event) {


        System.out.println(
                "Notify Fixed Income Trader. RFQ: "
                        + event.rfqId()
        );

    }
}