# Inventory Service

Inventory management microservice for the Retail Platform.

## Technology
- Java 21
- Spring Boot 3.5.7
- Spring Web, Spring Data JPA, Bean Validation
- Microsoft SQL Server
- Flyway migrations
- JUnit 5 and Mockito
- Spring Boot Actuator

## Database setup

This service uses the existing local SQL Server container from Product Service (`localhost:1433`).
Create a separate database named `inventory_db` in SQL Server Management Studio:

```sql
CREATE DATABASE inventory_db;
```

Do not point this service at `product_db`. Each microservice owns its database.
The local default credentials are for development only. Use environment variables for real credentials.

Flyway runs `src/main/resources/db/migration/V1__create_inventory_table.sql` on the empty database.
Hibernate is configured with `ddl-auto: validate`.

## Run

From this directory:

```powershell
mvn clean test
mvn spring-boot:run
```

Service URL: `http://localhost:8082`

Health: `http://localhost:8082/actuator/health`

## REST API

Base path: `/api/v1/inventory`

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/v1/inventory` | Create inventory record for a product |
| GET | `/api/v1/inventory` | List inventory |
| GET | `/api/v1/inventory/{productId}` | Get inventory by product ID |
| PATCH | `/api/v1/inventory/{productId}/stock` | Adjust on-hand stock by a signed delta |
| POST | `/api/v1/inventory/{productId}/reservations` | Reserve available stock |
| POST | `/api/v1/inventory/{productId}/reservations/release` | Release previously reserved stock |
| GET | `/api/v1/inventory/{productId}/availability?quantity=2` | Check available stock |

## Example requests

Create inventory:
```json
{
  "productId": 1,
  "sku": "LAPTOP-001",
  "initialQuantity": 50
}
```

Adjust stock (positive adds stock, negative removes stock):
```json
{
  "quantityDelta": 10
}
```

Reserve stock:
```json
{
  "quantity": 3
}
```

Release reservation:
```json
{
  "quantity": 2
}
```

Available quantity is `quantityOnHand - quantityReserved`.
The service prevents reservations beyond available stock and prevents stock adjustments below the reserved quantity.

## Design notes

- `productId` and `sku` are unique within this service's database.
- This service stores product identifiers and SKU; it does not directly own or modify Product Service data.
- `@Version` enables optimistic locking to detect concurrent updates.
- Reservation/release operations are local database transactions. Cross-service order workflows will be coordinated by Order Service (eventual consistency / Saga), not by a distributed database transaction.
