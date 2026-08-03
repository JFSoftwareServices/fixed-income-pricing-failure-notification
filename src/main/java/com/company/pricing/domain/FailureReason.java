package com.company.pricing.domain;

/**
 * Reasons why pricing an RFQ may fail.
 */
public enum FailureReason {
    PRICING_TIMEOUT,
    NO_LIQUIDITY,
    INTERNAL_ERROR
}