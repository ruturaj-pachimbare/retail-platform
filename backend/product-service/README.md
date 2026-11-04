# Product Service

Retail Platform product catalogue microservice using Java 21, Spring Boot, and Microsoft SQL Server.

## Requirements
- Java 21
- Maven 3.9+
- Docker Desktop

## 1. Start SQL Server
From this directory:
```powershell
docker compose up -d
```

The SQL Server container listens on `localhost:1433`. It uses the SQL Server Developer edition, suitable for local development.

## 2. Create the database
Connect using SQL Server Management Studio (SSMS) or Azure Data Studio:
- Server: `localhost,1433`
- Authentication: SQL Server Authentication
- Login: `sa`
- Password: `RetailDev_Passw0rd!`
- Encryption: enable encryption and trust the server certificate for this local development instance.

Run:
```sql
CREATE DATABASE product_db;
GO
```

Hibernate creates/updates the `products` table when the service starts.

Alternatively, if `sqlcmd` is installed locally:
```powershell
sqlcmd -S localhost,1433 -U sa -P "RetailDev_Passw0rd!" -C -Q "CREATE DATABASE product_db"
```

## 3. Run tests and service
```powershell
mvn clean test
mvn spring-boot:run
```

Service base URL: `http://localhost:8081`

Health: `http://localhost:8081/actuator/health`

## API
- `POST /api/v1/products`
- `GET /api/v1/products`
- `GET /api/v1/products/{id}`
- `PUT /api/v1/products/{id}`
- `DELETE /api/v1/products/{id}`
- `GET /api/v1/products?search=keyboard&page=0&size=10`

## Sample request
```json
{
  "name": "Mechanical Keyboard",
  "description": "RGB mechanical keyboard",
  "category": "Accessories",
  "sku": "KB-100",
  "price": 2499.00,
  "active": true
}
```

## Important
This project uses `ddl-auto: update` only for local learning. For production, use Flyway or Liquibase migrations and externalized secrets. Change the sample SQL Server password before using this outside local development.


## Database migrations with Flyway

Migration scripts live in `src/main/resources/db/migration`.
- `V1__create_products_table.sql` creates the products table for a new database.
- Flyway records applied migrations in `flyway_schema_history`.
- Hibernate uses `ddl-auto: validate`; it validates mappings but does not change the schema.

This project previously used Hibernate `ddl-auto: update`. For that existing local database,
`baseline-on-migrate: true` with baseline version `1` tells Flyway to adopt the existing schema
as version 1 rather than trying to create the already-existing table again. Confirm that the
existing `products` table matches the V1 definition before starting the service.

For a fresh empty `product_db`, Flyway executes V1 and creates the table.
