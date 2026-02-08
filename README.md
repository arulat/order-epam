# Orders API demo

Orders Management REST API built with Spring Boot 4.0.2 and Java 21.

## Features

- **Create orders** — POST /orders
- **List orders with pagination** — GET /orders?page=1&limit=10
- **Filter orders** by status, amount range, and date range
- **Auto-seed** 50 sample orders on startup if database is empty
- **H2 in-memory database** for local development and testing

## API Endpoints

| Method | Endpoint    | Description                     |
|--------|-------------|---------------------------------|
| POST   | `/orders`   | Create a new order              |
| GET    | `/orders`   | List orders with filters        |

### Query Parameters (GET /orders)

| Parameter   | Type    | Description                          |
|-------------|---------|--------------------------------------|
| `page`      | int     | Page number (1-based, default: 1)    |
| `limit`     | int     | Items per page (max: 100, default: 10)|
| `status`    | string  | Filter by order status               |
| `minAmount` | decimal | Minimum order amount                 |
| `maxAmount` | decimal | Maximum order amount                 |
| `dateFrom`  | instant | Filter orders from this date         |
| `dateTo`    | instant | Filter orders until this date        |

## Run tests

```zsh
./gradlew test
```

## Run tests with coverage

```zsh
./gradlew test jacocoTestReport
```

Coverage report: `build/reports/jacoco/test/html/index.html`

## Run app

```zsh
./gradlew bootRun
```

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ordersdb`
- Username: `sa`
- Password: (empty)

## Swagger UI

- UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs
