package com.example.demo.api.dto;

import java.util.List;

public class PagedResponse<T> {

  private List<T> items;
  private int page;
  private int limit;
  private long totalItems;
  private int totalPages;

  public PagedResponse(List<T> items, int page, int limit, long totalItems, int totalPages) {
    this.items = items;
    this.page = page;
    this.limit = limit;
    this.totalItems = totalItems;
    this.totalPages = totalPages;
  }

  public List<T> getItems() {
    return items;
  }

  public int getPage() {
    return page;
  }

  public int getLimit() {
    return limit;
  }

  public long getTotalItems() {
    return totalItems;
  }

  public int getTotalPages() {
    return totalPages;
  }
}

