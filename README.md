# Orders API demo

Orders Management REST API built with Spring Boot 4.0.2 and Java 21.

## Features

- **Create orders** — `POST /api/orders`
- **List orders with pagination** — `GET /api/orders?page=1&limit=10`
- **Filter orders** by status, amount range, and date range
- **Auto-seed** 50 sample orders on startup if database is empty
- **H2 in-memory database** for local development and testing

## API Endpoints

| Method | Endpoint       | Description                     |
|--------|----------------|---------------------------------|
| POST   | `/api/orders`  | Create a new order              |
| GET    | `/api/orders`  | List orders with filters        |

---

### GET /api/orders

Returns a paginated, optionally filtered list of orders sorted by `createdAt` descending.

#### Query Parameters

| Parameter   | Type      | Required | Default | Constraints              | Description                                      |
|-------------|-----------|----------|---------|--------------------------|--------------------------------------------------|
| `page`      | int       | No       | `1`     | >= 1                     | Page number (1-based)                            |
| `limit`     | int       | No       | `10`    | >= 1, <= 100             | Items per page                                   |
| `status`    | string    | No       | —       | One of: `NEW`, `PAID`, `SHIPPED`, `CANCELLED` | Filter by order status        |
| `minAmount` | decimal   | No       | —       | > 0, must be <= `maxAmount` if both provided  | Minimum order amount (inclusive) |
| `maxAmount` | decimal   | No       | —       | > 0, must be >= `minAmount` if both provided  | Maximum order amount (inclusive) |
| `dateFrom`  | ISO-8601  | No       | —       | Must be <= `dateTo` if both provided          | Start of date range (inclusive)  |
| `dateTo`    | ISO-8601  | No       | —       | Must be >= `dateFrom` if both provided        | End of date range (inclusive)    |

#### Request Examples

Default (first page, 10 items):

```
GET /api/orders
```

Custom page and limit:

```
GET /api/orders?page=2&limit=25
```

Filter by status:

```
GET /api/orders?status=PAID
```

Filter by amount range:

```
GET /api/orders?minAmount=50&maxAmount=200
```

Filter by date range:

```
GET /api/orders?dateFrom=2026-01-01T00:00:00Z&dateTo=2026-01-31T23:59:59Z
```

Combined filters with pagination:

```
GET /api/orders?status=SHIPPED&minAmount=100&page=1&limit=20
```

#### Successful Response (200 OK)

```json
{
  "items": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "status": "PAID",
      "amount": 149.99,
      "createdAt": "2026-01-15T10:30:00Z"
    },
    {
      "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "status": "PAID",
      "amount": 75.00,
      "createdAt": "2026-01-14T08:15:00Z"
    }
  ],
  "page": 1,
  "limit": 10,
  "totalItems": 2,
  "totalPages": 1
}
```

#### Empty Result (200 OK)

When filters match no orders, the response still returns 200 with an empty items array:

```json
{
  "items": [],
  "page": 1,
  "limit": 10,
  "totalItems": 0,
  "totalPages": 0
}
```

#### Validation Errors (400 Bad Request)

Invalid or out-of-range parameters return 400 with an error body.

`page=0` or `limit=0` (below minimum):

```json
{
  "message": "Invalid request parameters",
  "detail": "listOrders.page: must be greater than or equal to 1"
}
```

`limit=101` (above maximum):

```json
{
  "message": "Invalid request parameters",
  "detail": "listOrders.limit: must be less than or equal to 100"
}
```

`minAmount=200&maxAmount=100` (inverted range):

```json
{
  "message": "minAmount must be <= maxAmount"
}
```

`dateFrom=2026-02-10T00:00:00Z&dateTo=2026-02-01T00:00:00Z` (inverted date range):

```json
{
  "message": "dateFrom must be <= dateTo"
}
```

`status=UNKNOWN` (invalid enum value):

```json
{
  "message": "Invalid request parameters",
  "detail": "..."
}
```

---

### POST /api/orders

Creates a new order.

#### Request Body

```json
{
  "status": "NEW",
  "amount": 123.45
}
```

| Field    | Type    | Required | Constraints             |
|----------|---------|----------|-------------------------|
| `status` | string  | Yes      | One of: `NEW`, `PAID`, `SHIPPED`, `CANCELLED` |
| `amount` | decimal | Yes      | Must be > 0            |

#### Successful Response (201 Created)

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "status": "NEW",
  "amount": 123.45,
  "createdAt": "2026-02-10T12:00:00Z"
}
```

---

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

The API is available at `http://localhost:8080/api/orders`.

## H2 Console

- URL: http://localhost:8080/api/h2-console
- JDBC URL: `jdbc:h2:mem:ordersdb`
- Username: `sa`
- Password: (empty)

## Swagger UI

- UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI: http://localhost:8080/api/v3/api-docs
