package com.company.pricing.domain;

import java.time.Instant;
import java.util.UUID;

public record PricingFailureEvent(

                UUID eventId,

                String rfqId,

                String instrument,

                AssetClass assetClass,

                FailureReason failureReason,

                Instant occurredAt

) {
}