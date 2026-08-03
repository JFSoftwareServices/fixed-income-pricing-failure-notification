package com.company.pricing.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event published when pricing an RFQ fails.
 */
public record PricingFailureEvent(
        UUID eventId,
        String rfqId,
        String instrument,
        AssetClass assetClass,
        FailureReason failureReason,
        Instant occurredAt) {
}