package com.example.demo.service;

import com.example.demo.api.dto.CreateOrderRequest;
import com.example.demo.api.dto.OrderResponse;
import com.example.demo.api.dto.PagedResponse;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.repo.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Transactional
  public OrderResponse createOrder(CreateOrderRequest request) {
    Order order = new Order();
    order.setStatus(request.getStatus());
    order.setAmount(request.getAmount());
    order.setCreatedAt(Instant.now());
    Order saved = orderRepository.save(order);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public PagedResponse<OrderResponse> listOrders(
      int page,
      int limit,
      OrderStatus status,
      BigDecimal minAmount,
      BigDecimal maxAmount,
      Instant dateFrom,
      Instant dateTo) {

    validateRanges(minAmount, maxAmount, dateFrom, dateTo);
    Specification<Order> spec = Specification.unrestricted();

    if (status != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
    }
    if (minAmount != null) {
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
    }
    if (maxAmount != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
    }
    if (dateFrom != null) {
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom));
    }
    if (dateTo != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), dateTo));
    }

    PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<Order> result = orderRepository.findAll(spec, pageRequest);
    List<OrderResponse> items = result.getContent().stream().map(this::toResponse).toList();
    return new PagedResponse<>(items, page, limit, result.getTotalElements(), result.getTotalPages());
  }

  private void validateRanges(
      BigDecimal minAmount,
      BigDecimal maxAmount,
      Instant dateFrom,
      Instant dateTo) {

    if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
      throw new IllegalArgumentException("minAmount must be <= maxAmount");
    }
    if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
      throw new IllegalArgumentException("dateFrom must be <= dateTo");
    }
    if (Objects.equals(minAmount, BigDecimal.ZERO) || Objects.equals(maxAmount, BigDecimal.ZERO)) {
      throw new IllegalArgumentException("amount filters must be > 0");
    }
  }

  private OrderResponse toResponse(Order order) {
    return new OrderResponse(order.getId(), order.getStatus(), order.getAmount(), order.getCreatedAt());
  }
}
