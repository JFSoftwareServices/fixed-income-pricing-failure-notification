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

The solution uses event-driven architecture.

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

It does not know who is interested in that event.

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

The Recipient List pattern allows one message to be delivered to multiple recipients.

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

Adding a new recipient requires changing the producer.

---

# Kafka Recipient List Implementation

Kafka provides a loosely coupled implementation of the same concept.

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

Kafka topics and independent consumer groups provide the same outcome as the Recipient List pattern.

Each consumer group receives its own copy of the event while remaining independent from the producer.

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

---

# Development Environment vs Test Infrastructure

This project uses both **Dev Containers** and **Testcontainers**.

Although both use Docker, they solve different problems.

---

# Dev Container

## Purpose

A Dev Container provides a consistent development environment for engineers.

It is used while writing and running the application.

The developer does not need to install Java, Maven, or other tooling locally.

Architecture:

```
Developer

    |

    |

GitHub Codespace

    |

    |

Dev Container

    |

    +----------------+
    |                |
    v                v

 Java 21          Maven

 VS Code          Docker Tools

```

The Dev Container provides:

- Java 21
- Maven
- VS Code Java extensions
- Docker support
- Consistent developer environment

Configuration:

```
.devcontainer/

    |

    +-- devcontainer.json

    |

    +-- Dockerfile
```

---

# Testcontainers

## Purpose

Testcontainers provides disposable infrastructure for automated tests.

It is used when executing integration tests.

Instead of mocking external systems, the tests run against real infrastructure inside Docker containers.

Example:

```
JUnit 5 Integration Test

          |

          |

Spring Boot Application

          |

          |

Testcontainers

          |

          |

Kafka Docker Container

          |

          |

Assertions

```

---

# Why Use Testcontainers?

A mocked Kafka test:

```
Application

      |

      |

Mock Kafka Object

      |

      |

Test Result

```

Only verifies Java interactions.

It does not verify:

- Kafka connectivity
- Serialization
- Consumer configuration
- Topic communication
- Consumer groups

---

A Testcontainers integration test:

```
BDD Test

      |

      |

Spring Boot Application

      |

      |

Real Kafka Broker

running inside Docker

      |

      |

Test Assertions

```

This provides:

- Production-like testing
- Repeatable environments
- No manual Kafka installation
- Real message communication

---

# How Dev Containers and Testcontainers Work Together

The complete development workflow:

```
Developer

     |

     |

GitHub Codespace

     |

     |

Dev Container

(Java 21 + Maven)

     |

     |

Spring Boot Application


During Integration Tests:


JUnit 5

     |

     |

Testcontainers

     |

     |

Kafka Container

     |

     |

Kafka Broker


```

The developer environment and test infrastructure are separated.

---

# Testcontainers Lifecycle

During an integration test:

```
1. Test starts

        |

        v

2. Testcontainers starts Kafka container

        |

        v

3. Spring Boot connects to Kafka

        |

        v

4. Test publishes PricingFailureEvent

        |

        v

5. Consumers receive event

        |

        v

6. Assertions execute

        |

        v

7. Kafka container is destroyed

```

Each test execution starts with a clean environment.

---

# Production Similarity

Production:

```
Spring Boot Application

        |

        |

Kafka Broker

        |

        |

Trader Notification Services

```

Testing:

```
Spring Boot Application

        |

        |

Kafka Testcontainer

        |

        |

Trader Notification Consumers

```

The application communicates using the same Kafka protocol.

The only difference is that testing uses a temporary Kafka broker.

---

# Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Application runtime |
| Spring Boot | Application framework |
| Kafka | Event messaging |
| Dev Containers | Development environment |
| Testcontainers | Integration test infrastructure |
| Docker | Container runtime |
| JUnit 5 | Test execution |
| GitHub Codespaces | Cloud development environment |

---

# Opening in GitHub Codespaces

1. Open repository

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

Verify:

```bash
java -version
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
│   │   └── java/com/company/pricing
│   │
│   │       ├── config
│   │       │
│   │       ├── domain
│   │       │
│   │       ├── messaging
│   │       │   ├── publisher
│   │       │   └── consumer
│   │       │
│   │       └── notification
│   │
│   └── test
│
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

## Development

```
Spring Boot Application

        |

        |

Development Kafka Broker
```

## Testing

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

# Kafka Producer Implementation

The Pricing Engine publishes a `PricingFailureEvent` when automatic pricing fails.

The producer only knows about the event.

It does not know:

- who consumes the event
- how many consumers exist
- what actions are performed

Example:

```
Pricing Engine

       |

       |

PricingFailureEventPublisher

       |

       |

Kafka Topic

pricing-failure-events
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

Using RFQ ID as the Kafka key provides ordering for events belonging to the same RFQ.

Example:

```
RFQ-10001

 |

 +--> Pricing attempt failed

 |

 +--> Retry failed

 |

 +--> Manual intervention required
```

Kafka guarantees ordering within a partition.

---

# Kafka Consumers

The platform contains two independent consumers.

Both consume:

```
pricing-failure-events
```

However, they use different consumer groups.

```
              pricing-failure-events


                    |

        +-----------+-----------+

        |                       |

        v                       v


sales-trader              fixed-income-trader

```

Both traders receive their own copy of the event.

---

# Notification Service Layer

Kafka consumers are responsible only for receiving messages.

They should not contain business logic.

The notification responsibility is delegated to a service layer.

Architecture:

```
Kafka Topic

      |

      |

Kafka Consumer

      |

      |

TraderNotificationService

      |

      |

Notification Delivery
```

Benefits:

- Separation of responsibilities
- Easier unit testing
- Independent business logic
- Easier extension of notification channels

Future channels:

- Trading application alerts
- Email
- Chat notifications
- Mobile notifications
- Audit notifications

---

# Complete System Flow

```
Client

 |

 v

RFQ Request

 |

 v

Pricing Engine

 |

 X Automatic Pricing Failed

 |

 v

PricingFailureEvent

 |

 v

Kafka Topic

pricing-failure-events

 |

 +-----------------------+

 |                       |

 v                       v


Sales Trader        Fixed Income Trader

Consumer            Consumer

 |                       |

 v                       v


TraderNotificationService
```

---

# Testcontainers

## Why Testcontainers?

Testcontainers allows integration tests to run against real infrastructure inside Docker containers.

This project uses Testcontainers to start a real Kafka broker during automated tests.

It does not mock Kafka.

---

## Mock Approach

```
Application

     |

Fake Kafka Object

     |

Test
```

This only verifies code interaction.

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

# BDD Integration Test

The main acceptance test verifies:

```gherkin
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

Build Application

   |

Run Unit Tests

   |

Start Kafka Testcontainer

   |

Run Integration Tests

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
- Increased dependencies

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

# Interview Discussion Points

This project demonstrates:

## Event Driven Architecture

The pricing engine publishes business events rather than directly calling downstream services.

## Kafka

Kafka provides scalable asynchronous communication.

## Enterprise Integration Patterns

The Recipient List pattern allows multiple consumers to receive the same event.

## Testcontainers

Testcontainers provides production-like integration testing without external infrastructure dependencies.

---

# Author

JFSoftwareServices