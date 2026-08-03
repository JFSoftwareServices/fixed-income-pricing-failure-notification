package com.company.pricing.bdd;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * JUnit 5 test suite runner for Cucumber BDD integration tests.
 *
 * Discovers feature files from the classpath and uses the specified glue
 * package to locate step definitions and Cucumber Spring configuration.
 */
@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.company.pricing.bdd"
)
public class PricingFailureNotificationIntegrationTest {

}