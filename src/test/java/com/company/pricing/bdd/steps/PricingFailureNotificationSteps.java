package com.company.pricing.bdd.steps;

import com.company.pricing.domain.AssetClass;
import com.company.pricing.domain.FailureReason;
import com.company.pricing.domain.PricingFailureEvent;
import com.company.pricing.messaging.publisher.PricingFailureEventPublisher;
import com.company.pricing.bdd.support.RecordingTraderNotificationService;

import io.cucumber.java.en.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PricingFailureNotificationSteps {

    private final PricingFailureEventPublisher publisher;
    private final RecordingTraderNotificationService recorder;

    private PricingFailureEvent event;

    public PricingFailureNotificationSteps(
            PricingFailureEventPublisher publisher,
            RecordingTraderNotificationService recorder) {

        this.publisher = publisher;
        this.recorder = recorder;
    }

    @Given("automatic pricing has failed for an RFQ")
    public void pricingFailed() {
        event = new PricingFailureEvent(
                UUID.randomUUID(),
                "RFQ-10001",
                "UK GILT",
                AssetClass.FIXED_INCOME,
                FailureReason.PRICING_TIMEOUT,
                Instant.now()
        );
    }

    @When("the pricing failure event is published")
    public void publishEvent() {
        publisher.publish(event);
    }

    @Then("the Sales Trader should be notified")
    public void salesTraderNotification() {
        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() ->
                    assertTrue(recorder.salesTraderNotifiedFor(event.rfqId()))
                );
    }

    @Then("the Fixed Income Trader should be notified")
    public void fixedIncomeNotification() {
        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() ->
                    assertTrue(recorder.fixedIncomeTraderNotifiedFor(event.rfqId()))
                );
    }
}
