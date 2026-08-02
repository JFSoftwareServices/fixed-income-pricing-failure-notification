package com.company.pricing.bdd.support;

import com.company.pricing.domain.PricingFailureEvent;
import com.company.pricing.notification.TraderNotificationService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RecordingTraderNotificationService extends TraderNotificationService {

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
