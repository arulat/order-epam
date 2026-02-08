---
name: junit-mockmvc
description: Best practices for deterministic Spring Boot tests using MockMvc, including pagination and filters.
---

## When to use
Use this skill when generating or reviewing tests for controllers/services and validating JSON responses.

## Testing approach
- Prefer:
  - `@SpringBootTest`
  - `@AutoConfigureMockMvc`
- Use repository to seed deterministic data in `@BeforeEach`
- Use `jsonPath` assertions:
  - `$.items.length()`
  - `$.page`, `$.limit`, `$.totalItems`, `$.totalPages`

## Must-have test coverage
- POST:
  - 201 on valid request
  - 400 on invalid request (amount <= 0, missing status)
- GET:
  - default pagination
  - page/limit variations
  - limit bounds (too large / zero)
  - filters: status, min/max amount, date range, combos
  - invalid params -> 400

## Commands
- Tests: `./gradlew test`
- Coverage: `./gradlew test jacocoTestReport`
- Report: `build/reports/jacoco/test/html/index.html`
