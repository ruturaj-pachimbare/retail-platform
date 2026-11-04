# Notification Service

Spring Boot 3.5.7 / Java 21. Consumes order events from Kafka, records notification history in its own SQL Server database, and exposes read APIs.

## Exact Order Service event contract
Verified against `com.retail.order.dto.OrderEvent` and `OrderPersistenceService`: the event is flat JSON, not nested under `data` or `payload`:
`eventId`, `eventType`, `orderId`, `customerId`, `productId`, `quantity`, `totalAmount`, `status`, `occurredAt`.

Topic: `retail.order.events`. Consumer group: `retail-notification-service`.

## Prerequisites
- JDK 21 and Maven
- Existing SQL Server Docker container on localhost:1433
- Kafka broker on localhost:9092
- Empty database `notification_db`

Create database in SSMS: `CREATE DATABASE notification_db;`
Reuse the existing SQL Server container. Default local credentials are `sa` / `RetailDev_Passw0rd!`; override `DB_PASSWORD` if yours differs.

## Run
From this folder:
```powershell
mvn clean test
mvn spring-boot:run
```
Health: http://localhost:8084/actuator/health

## APIs
- GET /api/v1/notifications
- GET /api/v1/notifications/{id}
- GET /api/v1/notifications/order/{orderId}
- GET /api/v1/notifications/customer/{customerId}

## End-to-end test
Start SQL Server, Kafka, Notification Service, Inventory Service and Order Service. Create an order through the Order Service API. The Order Service outbox publisher sends the event to `retail.order.events`. Query `/api/v1/notifications/order/{orderId}` and expect a notification record.

The event ID is checked before insert and protected by a unique database constraint for idempotency. Delivery is currently simulated and marked `SIMULATED`; this version does not send real email or SMS.

## Environment overrides
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `KAFKA_BOOTSTRAP_SERVERS`, `KAFKA_GROUP_ID`, `ORDER_EVENTS_TOPIC`.
