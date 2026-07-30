# Fixed Income Pricing Failure Notification

![Java 21](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-Event%20Driven-black)
![Testcontainers](https://img.shields.io/badge/Testcontainers-Integration%20Testing-blue)

---

# Overview

This project demonstrates an event-driven notification workflow within a fixed-income trading platform.

The business scenario:

> When an automatic pricing attempt fails, the Sales Trader and Fixed Income Trader must be notified.

The project demonstrates:

- Java 21
- Spring Boot
- Apache Kafka
- Enterprise Integration Patterns (EIP)
- Recipient List pattern
- Event-driven architecture
- BDD integration testing
- Testcontainers
- GitHub Codespaces
- GitHub Actions CI/CD

---

# Business Context

A fixed-income trading platform allows institutional clients to submit RFQs (Request For Quote).

Example workflow:

```
Client

   |
   |
   v

RFQ Request

   |
   |
   v

Pricing Engine

   |
   |
   v

Automatic Pricing Attempt
```

The pricing engine attempts to automatically calculate a tradable price.

If successful:

```
RFQ

 |

Price Generated

 |

Trader receives quote
```

If automatic pricing fails:

```
RFQ

 |

Automatic Pricing Failure

 |

PricingFailureEvent

 |

+---------------------+
|                     |
v                     v

Sales Trader          Fixed Income Trader

Notification          Notification
```

This project focuses on the failure notification workflow.

---

# Architecture Overview

The solution uses an event-driven architecture.

The pricing engine publishes a single business event when automatic pricing fails.

The pricing engine does not know who consumes the event.

```
                    Pricing Engine

                         |
                         |
                         v

              PricingFailureEvent

                         |
                         |
                         v

                  Kafka Topic

          pricing-failure-events

                         |
             +-----------+-----------+
             |                       |
             v                       v

     Sales Trader Consumer   Fixed Income Trader Consumer

```

---

# Design Goal

The pricing engine publishes one event.

It does not know who is interested in the event.

Example:

```
Pricing Engine

        |

        |

PricingFailureEvent

        |

        |

Kafka Topic

        |

        |

Multiple Independent Consumers
```

Adding a new consumer should not require changing the pricing engine.

Future consumers could include:

- Operations Dashboard
- Audit Service
- Monitoring Service
- Regulatory Reporting Service

---

# Enterprise Integration Pattern

## Recipient List Pattern

The Recipient List pattern allows a message to be delivered to multiple recipients.

Traditional implementation:

```
                 Pricing Engine

                       |

        +--------------+--------------+

        |                             |

        v                             v

Sales Trader                  Fixed Income Trader

```

The problem:

The producer becomes tightly coupled to every recipient.

If a new recipient is added, the producer must change.

---

# Kafka Recipient List Implementation

Kafka provides a more loosely coupled implementation.

The producer publishes one event:

```
PricingFailureEvent

        |

        |

pricing-failure-events

```

Multiple consumers independently subscribe:

```
                 pricing-failure-events


                         |

          +--------------+--------------+

          |                             |

          v                             v


Sales Trader Consumer          Fixed Income Consumer


Consumer Group:               Consumer Group:

sales-trader                  fixed-income-trader

```

Kafka consumer groups allow each trader service to receive its own copy of the event.

---

# Why Kafka?

Kafka was selected because it provides:

- Loose coupling
- Asynchronous communication
- Durable event storage
- Multiple independent consumers
- High throughput messaging

Kafka allows new consumers to be added without changing existing producers.

---

# Domain Model

## PricingFailureEvent

The event represents a business fact:

> Automatic pricing failed for an RFQ.

The event contains:

```
eventId

rfqId

instrument

assetClass

failureReason

occurredAt
```

The event is immutable and implemented using Java 21 Records.

Example:

```json
{
  "eventId": "7d7c8b9a",
  "rfqId": "RFQ-10001",
  "instrument": "UK GILT",
  "assetClass": "FIXED_INCOME",
  "failureReason": "PRICING_TIMEOUT",
  "occurredAt": "2026-07-29T10:30:00Z"
}
```

---

# Development Environment

This project uses GitHub Codespaces with a Dev Container.

The container provides:

- Java 21
- Maven
- VS Code Java extensions
- Docker support

The application can be developed without installing Java locally.

---

# Opening in GitHub Codespaces

1. Open the repository.

2. Select:

```
Codespaces → Create codespace
```

3. The environment automatically provides:

```
Java 21

Maven

VS Code Java tooling

Docker support
```

Verify Java:

```bash
java -version
```

Expected:

```
java version "21.x.x"
```

Verify Maven:

```bash
mvn -version
```

---

# Project Structure

```
fixed-income-pricing-failure-notification

├── .devcontainer
│   ├── devcontainer.json
│   └── Dockerfile
│
├── .github
│   └── workflows
│
├── docs
│   ├── architecture
│   └── diagrams
│
├── src
│
│   ├── main
│   │
│   │   ├── config
│   │   ├── controller
│   │   ├── domain
│   │   ├── messaging
│   │   ├── notification
│   │   └── service
│   │
│   └── test
│
├── docker-compose.yml
├── pom.xml
└── README.md

```

---

# Kafka Configuration

Kafka configuration is externalised.

Example:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

The application does not contain hard-coded infrastructure details.

---

# Environment Configuration

Configuration is separated by environment.

```
src/main/resources

├── application.yml

├── application-dev.yml

└── application-test.yml
```

## application.yml

Contains common application configuration.

## application-dev.yml

Used for development.

Example:

```
Spring Boot Application

        |

        |

Development Kafka Broker
```

## application-test.yml

Used during integration testing.

Kafka is supplied dynamically by Testcontainers.

```
Integration Test

        |

        |

Kafka Testcontainer

        |

        |

Temporary Kafka Broker
```

---

# Testcontainers

## Why Testcontainers?

Testcontainers allows integration tests to run against real infrastructure inside Docker containers.

This project uses Testcontainers to start a real Kafka broker during automated tests.

It does not mock Kafka.

## Mock Approach

```
Application

     |

Fake Kafka Object

     |

Test
```

The test verifies code interaction only.

---

## Testcontainers Approach

```
BDD Integration Test

          |

          |

Spring Boot Application

          |

          |

Real Kafka Broker

running inside Docker

          |

          |

Assertions
```

Benefits:

- Production-like testing
- Repeatable environment
- No manual Kafka installation
- Same communication protocol as production

---

# Testing Strategy

The main acceptance test is:

```gherkin
@API
@Integration
@Kafka

Scenario: Notify traders when automatic pricing fails

Given automatic pricing has failed for an RFQ

When the pricing failure event is published

Then the Sales Trader should be notified

And the Fixed Income Trader should be notified
```

The test verifies:

- Event publication
- Kafka delivery
- Multiple consumers
- Recipient List behaviour

---

# Integration Test Flow

```
                    BDD Test

                       |

                       |

              Spring Boot Application

                       |

                       |

              KafkaTemplate Publisher

                       |

                       |

              Kafka Testcontainer

                       |

          +------------+------------+

          |                         |

          v                         v


Sales Trader Consumer       Fixed Income Consumer


          |                         |

          v                         v


     Assertion              Assertion

```

---

# Running the Application

```bash
mvn spring-boot:run
```

---

# Running Tests

```bash
mvn clean test
```

Integration tests automatically start Kafka using Testcontainers.

---

# CI/CD Pipeline

GitHub Actions executes:

```
Git Push

   |

   |

Build Application

   |

   |

Run Unit Tests

   |

   |

Start Kafka Testcontainer

   |

   |

Run Integration Tests

   |

   |

Publish Results

```

---

# Design Decisions

## Why events?

The pricing failure is a business event.

Consumers react to something that happened.

The producer does not issue commands to specific consumers.

---

## Why not direct API calls?

Direct approach:

```
Pricing Engine

       |

       |

Sales Trader API

       |

       |

Fixed Income Trader API
```

Problems:

- Tight coupling
- Harder to add recipients
- Increased dependency between services

Kafka approach:

```
Pricing Engine

       |

       |

Kafka Event

       |

       |

Multiple Consumers
```

---

# Future Improvements

Potential enhancements:

- Dead Letter Queue handling
- Retry mechanism
- Kafka Schema Registry
- Avro/Protobuf messages
- Observability with metrics
- Kubernetes deployment
- Performance testing
- Contract testing

---

# Project Roadmap

## Completed

✅ Spring Boot foundation

✅ Java 21 Dev Container

✅ Domain event model

✅ Kafka infrastructure configuration


## Upcoming

⬜ Kafka publisher

⬜ Trader notification consumers

⬜ REST API

⬜ Testcontainers integration tests

⬜ BDD implementation

⬜ GitHub Actions pipeline

⬜ Architecture diagrams

---

# Interview Discussion Points

This project demonstrates:

## Event Driven Architecture

The pricing engine publishes business events rather than calling downstream services directly.

## Kafka

Kafka provides scalable asynchronous communication.

## Enterprise Integration Patterns

The Recipient List pattern allows multiple consumers to receive the same event.

---

# Kafka Producer Implementation

## Overview

The Pricing Engine now publishes a `PricingFailureEvent` to Kafka when an automatic pricing attempt fails.

The producer is responsible only for publishing the business event.

It does **not** know:

- who consumes the event
- how many consumers exist
- what actions are taken after the event is received

This keeps the pricing engine loosely coupled from downstream services.

---

# Producer Flow

The current implementation:

```
                Pricing Engine

                       |
                       |
                       v

        PricingFailureEventPublisher

                       |
                       |
                       v

              Kafka Topic

        pricing-failure-events

                       |
                       |
                       v

              Kafka Broker
```

At this stage, the project contains the Kafka publisher only.

Consumer services will be introduced in later commits.

---

# PricingFailureEventPublisher

The publisher uses Spring Kafka's `KafkaTemplate` to send events.

Example:

```java
@Component
public class PricingFailureEventPublisher {

    private final KafkaTemplate<String, PricingFailureEvent> kafkaTemplate;

    private final KafkaTopicsProperties topics;


    public PricingFailureEventPublisher(
            KafkaTemplate<String, PricingFailureEvent> kafkaTemplate,
            KafkaTopicsProperties topics) {

        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }


    public void publish(PricingFailureEvent event) {

        kafkaTemplate.send(
                topics.pricingFailure(),
                event.rfqId(),
                event
        );
    }
}
```

---

# Kafka Message Design

Kafka messages contain:

```
Key

RFQ ID


Value

PricingFailureEvent
```

Example:

```
Key:

RFQ-10001


Value:

{
  "eventId": "12345",
  "rfqId": "RFQ-10001",
  "instrument": "UK GILT",
  "assetClass": "FIXED_INCOME",
  "failureReason": "PRICING_TIMEOUT",
  "occurredAt": "2026-07-30T10:30:00Z"
}
```

---

# Why Use RFQ ID As The Kafka Key?

Kafka uses the message key to determine partition placement.

Using the RFQ ID provides ordering for events belonging to the same RFQ.

Example:

```
RFQ-10001

    |
    |
    +--> Pricing attempt failed
    |
    +--> Retry failed
    |
    +--> Manual intervention required
```

Kafka guarantees ordering within the partition.

---

# Loose Coupling Design

A tightly coupled approach would be:

```
Pricing Engine

       |

       +------------------> Sales Trader API

       |

       +------------------> Fixed Income Trader API
```

Problems:

- Pricing Engine knows every consumer
- Adding new consumers requires code changes
- More dependencies between systems

---

The event-driven approach:

```
Pricing Engine

       |

       |

PricingFailureEvent

       |

       |

Kafka Topic

       |

       +------------------> Sales Trader

       |

       +------------------> Fixed Income Trader

       |

       +------------------> Audit Service

```

Benefits:

- New consumers can subscribe without changing the producer
- Services evolve independently
- Better scalability and resilience

---

# Testing Strategy

The publisher contains unit tests verifying:

- The event is published
- The correct Kafka topic is used
- The RFQ ID is used as the message key

Example:

```
PricingFailureEventPublisherTest

        |

        |

Mock KafkaTemplate

        |

        |

Verify publish interaction
```

Integration testing with a real Kafka broker using Testcontainers will be introduced in later commits.

---

# Current Implementation Status

Completed:

✅ Spring Boot Kafka configuration

✅ Kafka topic externalised through configuration

✅ PricingFailureEventPublisher

✅ Publisher unit test


Upcoming:

⬜ Kafka consumers

⬜ Sales Trader notification service

⬜ Fixed Income Trader notification service

⬜ Recipient List implementation

⬜ BDD integration test

⬜ Kafka Testcontainers environment

## Testcontainers

Integration tests execute against real infrastructure instead of mocks.

## Testing Strategy

BDD scenarios verify business behaviour while unit tests verify individual components.

---

# Author

JFSoftwareServices