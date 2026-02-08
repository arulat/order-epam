package com.example.demo.seed;

import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.repo.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderSeeder implements CommandLineRunner {

  private final OrderRepository orderRepository;

  public OrderSeeder(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public void run(String... args) {
    if (orderRepository.count() > 0) {
      return;
    }

    Random random = new Random(42L);
    OrderStatus[] statuses = OrderStatus.values();
    List<Order> orders = new ArrayList<>();

    for (int i = 0; i < 50; i++) {
      Order order = new Order();
      order.setStatus(statuses[random.nextInt(statuses.length)]);
      order.setAmount(BigDecimal.valueOf(10 + random.nextInt(990)).setScale(2));
      order.setCreatedAt(Instant.now().minus(random.nextInt(60), ChronoUnit.DAYS));
      orders.add(order);
    }

    orderRepository.saveAll(orders);
  }
}

