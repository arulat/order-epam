---
name: scaffold-orders-api
description: Scaffold Spring Boot Orders API (entity, repository, service, controller, validation, error handling, seed).
argument-hint: "Optional: any extra fields you want in Order"
---

You are implementing a Spring Boot Orders Management API in Java 21 with Gradle.

Generate a minimal but complete implementation with:
1) Domain:
   - Order entity (UUID id, status enum, BigDecimal amount, createdAt timestamp)
   - OrderStatus enum: NEW, PAID, SHIPPED, CANCELLED

2) Persistence:
   - Spring Data JPA repository
   - Use H2 for local/dev/test

3) API:
   - POST /orders (validation: amount > 0, status required) returns 201
   - GET /orders with:
     - pagination: page (1-based) and limit (cap limit at 100)
     - filters: status, minAmount/maxAmount, dateFrom/dateTo
   - Response includes items + (page, limit, totalItems, totalPages)

4) Filtering implementation:
   - Use JpaSpecificationExecutor + Specifications (no manual SQL strings)

5) Error handling:
   - Centralized @RestControllerAdvice
   - Return 400 on invalid params or validation failures
   - No stack traces in responses

6) Seeding:
   - Seed 50 sample orders on startup if DB empty (CommandLineRunner)

Keep code concise and consistent with package structure:
- api, domain, repo, service, error, seed, config

Output:
- Provide the list of files with full code blocks per file.
- Do not include README or tests here (separate prompts exist for those).
