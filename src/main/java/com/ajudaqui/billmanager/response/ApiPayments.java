package com.ajudaqui.billmanager.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;

public class ApiPayments {
  private List<Payment> payments = new ArrayList<Payment>();
  private BigDecimal totalValue;

  public ApiPayments(List<Payment> payments) {
    this.payments = payments;
    this.totalValue = sumTotal(payments);
  }

  public List<Payment> getPayments() {
    return payments;
  }

  public void setPayments(List<Payment> payments) {
    this.payments = payments;
  }

  public BigDecimal getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(BigDecimal totalValue) {
    this.totalValue = totalValue;
  }

  public BigDecimal sumTotal(List<Payment> payments) {

    return payments.stream()
        .map(Payment::getValue)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

  }

}
