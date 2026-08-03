package com.company.pricing.bdd;

import com.company.pricing.PricingNotificationApplication;
import com.company.pricing.config.KafkaTestContainerConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Spring Boot configuration used by Cucumber integration tests.
 *
 * Loads the application context for BDD scenarios and configures Kafka
 * to use the Testcontainers Kafka broker instead of an external Kafka instance.
 */
@CucumberContextConfiguration
@SpringBootTest(classes = PricingNotificationApplication.class)
@ActiveProfiles("test")
@Import(KafkaTestContainerConfiguration.class)
public class CucumberSpringConfiguration {

    /**
     * Overrides Spring Kafka bootstrap servers with the dynamically created
     * Kafka Testcontainer endpoint.
     */
    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.kafka.bootstrap-servers",
                KafkaTestContainerConfiguration.KAFKA_CONTAINER::getBootstrapServers
        );
    }
}