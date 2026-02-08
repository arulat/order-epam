package com.example.demo;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.api.dto.CreateOrderRequest;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.repo.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTests {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private OrderRepository orderRepository;

  private final Instant baseTime = Instant.parse("2026-01-01T00:00:00Z");

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    orderRepository.saveAll(seedOrders());
  }

  @Test
  void createOrder_returns201() throws Exception {
    CreateOrderRequest request = new CreateOrderRequest();
    request.setStatus(OrderStatus.NEW);
    request.setAmount(new BigDecimal("123.45"));

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.status", is("NEW")))
        .andExpect(jsonPath("$.amount", is(123.45)))
        .andExpect(jsonPath("$.createdAt").exists());
  }

  @Test
  void createOrder_missingStatus_returns400() throws Exception {
    CreateOrderRequest request = new CreateOrderRequest();
    request.setAmount(new BigDecimal("10.00"));

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createOrder_amountZero_returns400() throws Exception {
    CreateOrderRequest request = new CreateOrderRequest();
    request.setStatus(OrderStatus.NEW);
    request.setAmount(BigDecimal.ZERO);

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listOrders_defaultPagination_returnsMeta() throws Exception {
    mockMvc.perform(get("/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items", hasSize(20)))
        .andExpect(jsonPath("$.page", is(1)))
        .andExpect(jsonPath("$.limit", is(20)))
        .andExpect(jsonPath("$.totalItems", is(25)))
        .andExpect(jsonPath("$.totalPages", is(2)));
  }

  @Test
  void listOrders_pageAndLimit_returnsSecondPage() throws Exception {
    mockMvc.perform(get("/orders")
        .param("page", "2")
        .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items", hasSize(10)))
        .andExpect(jsonPath("$.page", is(2)))
        .andExpect(jsonPath("$.limit", is(10)))
        .andExpect(jsonPath("$.totalItems", is(25)))
        .andExpect(jsonPath("$.totalPages", is(3)));
  }

  @Test
  void listOrders_limitTooLarge_returns400() throws Exception {
    mockMvc.perform(get("/orders")
        .param("limit", "101"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listOrders_filterByStatus_returnsMatches() throws Exception {
    mockMvc.perform(get("/orders")
        .param("status", "PAID"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems", is(6)))
        .andExpect(jsonPath("$.items", hasSize(6)))
        .andExpect(jsonPath("$.items[*].status", everyItem(is("PAID"))));
  }

  @Test
  void listOrders_filterByAmountRange_returnsMatches() throws Exception {
    mockMvc.perform(get("/orders")
        .param("minAmount", "15")
        .param("maxAmount", "18"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems", is(4)))
        .andExpect(jsonPath("$.items", hasSize(4)))
        .andExpect(jsonPath("$.items[*].amount", everyItem(greaterThanOrEqualTo(15.0))))
        .andExpect(jsonPath("$.items[*].amount", everyItem(lessThanOrEqualTo(18.0))));
  }

  @Test
  void listOrders_filterByDateRange_returnsMatches() throws Exception {
    Instant from = baseTime.plus(10, ChronoUnit.DAYS);
    Instant to = baseTime.plus(12, ChronoUnit.DAYS);

    mockMvc.perform(get("/orders")
        .param("dateFrom", from.toString())
        .param("dateTo", to.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems", is(3)))
        .andExpect(jsonPath("$.items", hasSize(3)));
  }

  @Test
  void listOrders_filterCombination_returnsMatches() throws Exception {
    mockMvc.perform(get("/orders")
        .param("status", "SHIPPED")
        .param("minAmount", "12"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems", is(6)))
        .andExpect(jsonPath("$.items", hasSize(6)))
        .andExpect(jsonPath("$.items[*].status", everyItem(is("SHIPPED"))));
  }

  @Test
  void listOrders_invalidStatus_returns400() throws Exception {
    mockMvc.perform(get("/orders")
        .param("status", "UNKNOWN"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listOrders_minAmountGreaterThanMaxAmount_returns400() throws Exception {
    mockMvc.perform(get("/orders")
        .param("minAmount", "20")
        .param("maxAmount", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listOrders_dateFromAfterDateTo_returns400() throws Exception {
    mockMvc.perform(get("/orders")
        .param("dateFrom", "2026-02-10T00:00:00Z")
        .param("dateTo", "2026-02-01T00:00:00Z"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listOrders_pageZero_returns400() throws Exception {
    mockMvc.perform(get("/orders")
        .param("page", "0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listOrders_limitZero_returns400() throws Exception {
    mockMvc.perform(get("/orders")
        .param("limit", "0"))
        .andExpect(status().isBadRequest());
  }

  private List<Order> seedOrders() {
    List<Order> orders = new ArrayList<>();
    OrderStatus[] statuses = OrderStatus.values();
    for (int i = 0; i < 25; i++) {
      Order order = new Order();
      order.setStatus(statuses[i % statuses.length]);
      order.setAmount(BigDecimal.valueOf(10 + i));
      order.setCreatedAt(baseTime.plus(i, ChronoUnit.DAYS));
      orders.add(order);
    }
    return orders;
  }
}

