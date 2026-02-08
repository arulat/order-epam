package com.example.demo.api.dto;

import com.example.demo.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class OrderResponse {

  private UUID id;
  private OrderStatus status;
  private BigDecimal amount;
  private Instant createdAt;

  public OrderResponse(UUID id, OrderStatus status, BigDecimal amount, Instant createdAt) {
    this.id = id;
    this.status = status;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}

