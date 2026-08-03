package com.company.pricing.domain;

/**
 * Represents a request for quote (RFQ).
 */
public record Rfq(
                String rfqId,
                String instrument) {
}