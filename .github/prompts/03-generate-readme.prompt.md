---
name: generate-readme
description: Generate README.md for Orders API (setup, run, endpoints, examples, filters, tests, coverage).
argument-hint: "Optional: include deployment notes if needed"
---

Create a complete README.md for the Orders Management API.

Must include:
1) Overview (what the API does)
2) Requirements (Java 21)
3) How to run:
   - ./gradlew bootRun
4) Database:
   - H2 (in-memory), seeded 50 orders on startup if empty
5) API docs:
   - POST /orders
     - request JSON example
     - response JSON example
     - validation errors example
   - GET /orders
     - pagination: page, limit
     - filters: status, minAmount, maxAmount, dateFrom, dateTo
     - examples of query strings
     - response shape with pagination metadata
6) Testing:
   - ./gradlew test
   - Coverage:
     - ./gradlew test jacocoTestReport
     - path: build/reports/jacoco/test/html/index.html

Style:
- Clear, copy-paste friendly curl examples
- Keep it concise but complete
