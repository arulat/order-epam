# Orders API (Spring Boot, Java 21) — Copilot instructions

## Project goals
Build Orders Management REST API with pagination + filtering.
Must include: POST /orders, GET /orders?page&limit + filters (status, amount range, date range).
Seed 50 orders on startup if DB empty.

## Tech stack
- Java 21, Spring Boot
- Spring Web, Spring Data JPA, Validation
- H2 for local/dev and tests
- Tests: JUnit5 + MockMvc

## API conventions
- GET /orders uses 1-based `page` externally, convert to 0-based internally
- Enforce max page size: limit <= 100
- Response includes items + pagination metadata (page, limit, totalItems, totalPages)

## Security & quality
- Never build SQL strings manually (use JPA + Specifications)
- Validate inputs (amount > 0, dateFrom <= dateTo, minAmount <= maxAmount)
- Return 400 on invalid query params without stack traces
- Keep code small, readable, consistent package structure


## Metrics discipline
- Use commit prefixes: "copilot:" for mainly generated code, "manual:" for fixes/refactors.
- After each milestone, update:
    - metrics/suggestions.csv (shown/accepted)
    - metrics/time.csv (actual/estimate)
- Keep REPORT.md to 1 page; include the output of ./gradlew metricsReport.