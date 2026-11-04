# Retail Platform - Order Service

Spring Boot 3.5.7 / Java 21 microservice. Owns `order_db`, creates and cancels orders, reserves/releases stock through OpenFeign, and publishes order events to Kafka using a SQL Server transactional outbox.

## Prerequisites
JDK 21, Maven 3.9+, SQL Server on localhost:1433, Inventory Service on localhost:8082, Kafka on localhost:9092.
Create an empty SQL Server database named `order_db` in SSMS. Reuse the SQL Server container already used by Product and Inventory services.

## Inventory API integration contract
Default Feign routes:
- `POST /api/v1/inventory/{productId}/reservations` body `{"quantity": 2}`
- `POST /api/v1/inventory/{productId}/release` body `{"quantity": 2}`

These paths are configurable in `src/main/resources/application.yml`. **Before running, compare them with your InventoryController mappings and adjust the two path values and StockQuantityRequest if your Inventory API uses a different route or payload.** This is the integration point between the two services.

## Run
```powershell
cd backend/order-service
mvn clean test
mvn spring-boot:run
```
Health: `http://localhost:8083/actuator/health`

## REST API
Base URL: `http://localhost:8083/api/v1/orders`

Create order:
```http
POST /api/v1/orders
Content-Type: application/json

{"customerId":1001,"productId":1,"quantity":2,"unitPrice":49999.00}
```
Get all: `GET /api/v1/orders`
Get by id: `GET /api/v1/orders/{id}`
Customer orders: `GET /api/v1/orders/customer/{customerId}`
Cancel: `POST /api/v1/orders/{id}/cancel`

## Kafka / outbox
Topic: `retail.order.events`. Order row and outbox row are written in one local DB transaction. The scheduled publisher sends pending events and marks them published. Delivery is at-least-once; consumers should deduplicate using `eventId`.

## Distributed transaction note
There is no single ACID transaction spanning Order DB, Inventory DB and Kafka. The service compensates with a stock release if local order persistence fails, and uses an outbox for event delivery. A production workflow should additionally persist saga/reservation state to handle process crashes between remote inventory operations and local commits.
