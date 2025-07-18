package com.ajudaqui.billmanager.response;

import java.math.BigDecimal;
import java.util.Set;

import com.ajudaqui.billmanager.entity.Category;
import com.ajudaqui.billmanager.entity.Payment;

public class ApiCategory {
  private Long id;
  private String name;
  private BigDecimal total;

  public ApiCategory() {
  }

  public ApiCategory(Category category) {
    setId(category.getId());
    setName(category.getName());
    if (category.getPayments() != null)
      setTotal(category.getPayments());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(Set<Payment> payments) {
    this.total = payments.stream()
        .map(Payment::getValue)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
