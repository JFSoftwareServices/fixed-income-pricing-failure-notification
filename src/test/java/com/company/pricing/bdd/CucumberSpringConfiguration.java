package com.company.pricing.bdd;

import com.company.pricing.PricingNotificationApplication;
import com.company.pricing.config.KafkaTestContainerConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@CucumberContextConfiguration
@SpringBootTest(classes = PricingNotificationApplication.class)
@ActiveProfiles("test")
@Import(KafkaTestContainerConfiguration.class)
public class CucumberSpringConfiguration {

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.kafka.bootstrap-servers",
                KafkaTestContainerConfiguration.KAFKA_CONTAINER::getBootstrapServers
        );
    }
}