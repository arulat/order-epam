---
name: generate-orders-tests
description: Generate 12–15 MockMvc tests for Orders API (pagination, filters, validation, edge cases).
argument-hint: "Optional: mention any custom response fields"
---

Generate JUnit 5 + Spring Boot + MockMvc tests for the Orders API.

Requirements:
- 12–15 tests total
- Cover:
  - POST /orders happy path (201)
  - POST validation errors (amount <= 0, missing status) => 400
  - GET /orders default pagination (200 + meta)
  - GET /orders with page/limit (page 2, limit 10, etc.)
  - limit bounds: limit too large should be capped or rejected (match current implementation)
  - filters:
    - status
    - minAmount/maxAmount
    - dateFrom/dateTo
    - combinations of filters
  - invalid params:
    - invalid enum status => 400
    - minAmount > maxAmount => 400
    - dateFrom > dateTo => 400
    - page=0 or limit=0 => 400

Test design:
- Use H2 in-memory
- Prepare data using repository in @BeforeEach (insert a predictable dataset)
- Assert response JSON with jsonPath:
  - items length
  - returned status/amount in items
  - pagination metadata fields exist and are correct

Output:
- Provide one test class (or two if cleaner), full code only.
- Assume Gradle + Spring Boot Test is available.
- Keep tests deterministic (no flaky time-based assertions).
