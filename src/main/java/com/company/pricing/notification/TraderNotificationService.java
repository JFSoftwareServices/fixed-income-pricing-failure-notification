package com.company.pricing.notification;

import com.company.pricing.domain.PricingFailureEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Handles trader notifications triggered by pricing failure events.
 */
@Service
public class TraderNotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(TraderNotificationService.class);

    public void notifySalesTrader(PricingFailureEvent event) {
        log.info("Notify Sales Trader. RFQ: {}", event.rfqId());
    }

    public void notifyFixedIncomeTrader(PricingFailureEvent event) {
        log.info("Notify Fixed Income Trader. RFQ: {}", event.rfqId());
    }
}