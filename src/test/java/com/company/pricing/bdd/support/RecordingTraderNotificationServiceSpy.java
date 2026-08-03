package com.company.pricing.bdd.support;

import com.company.pricing.domain.PricingFailureEvent;
import com.company.pricing.notification.TraderNotificationService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Test spy implementation of TraderNotificationService used by BDD tests.
 *
 * This class preserves the real notification behaviour by delegating to the
 * parent service while recording notification interactions. The recorded
 * RFQ IDs allow tests to verify that the correct trader was notified after
 * a pricing failure event was consumed.
 */
public class RecordingTraderNotificationServiceSpy extends TraderNotificationService {

    private final List<String> salesTraderNotifications = new CopyOnWriteArrayList<>();
    private final List<String> fixedIncomeTraderNotifications = new CopyOnWriteArrayList<>();

    @Override
    public void notifySalesTrader(PricingFailureEvent event) {
        super.notifySalesTrader(event);
        salesTraderNotifications.add(event.rfqId());
    }

    @Override
    public void notifyFixedIncomeTrader(PricingFailureEvent event) {
        super.notifyFixedIncomeTrader(event);
        fixedIncomeTraderNotifications.add(event.rfqId());
    }

    public boolean salesTraderNotifiedFor(String rfqId) {
        return salesTraderNotifications.contains(rfqId);
    }

    public boolean fixedIncomeTraderNotifiedFor(String rfqId) {
        return fixedIncomeTraderNotifications.contains(rfqId);
    }
}
