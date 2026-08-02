Feature: Pricing failure notification

  Scenario: Notify traders when automatic pricing fails
    Given automatic pricing has failed for an RFQ
    When the pricing failure event is published
    Then the Sales Trader should be notified
    And the Fixed Income Trader should be notified