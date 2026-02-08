Plan: Scaffold Orders API

Create core domain, persistence, service, and API layers for the Orders API, then add validation, filtering with JPA specifications, error handling, and startup seeding. Follow the package structure and API conventions in the prompt, keep code minimal, and ensure pagination and filters behave correctly (1-based page, limit cap). This plan lists required files and the order to implement them while avoiding tests/README in this step.

Steps 3–6 steps, 5–20 words each
1. [ ] Add domain types in src/main/java/com/example/demo/domain/Order.java and src/main/java/com/example/demo/domain/OrderStatus.java.
2. [ ] Create repository and specifications in src/main/java/com/example/demo/repo/OrderRepository.java.
3. [ ] Implement service logic and filtering in src/main/java/com/example/demo/service/OrderService.java.
4. [ ] Add request/response models and controller in src/main/java/com/example/demo/api/OrderController.java and src/main/java/com/example/demo/api/dto.
5. [ ] Add validation and error handling in src/main/java/com/example/demo/error/ApiExceptionHandler.java and seed data in src/main/java/com/example/demo/seed/OrderSeeder.java.

Further Considerations 1–3, 5–25 words each
1. Confirm whether to save this plan into plan-scaffoldOrdersApi.prompt.md now or after your review.
2. keep dtos in domain layer

