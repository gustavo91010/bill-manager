package com.ajudaqui.billmanager.service.vo;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sumary {

  private BigDecimal totalDue = BigDecimal.ZERO;
  private BigDecimal amountPaid = BigDecimal.ZERO;
  private BigDecimal remaining = BigDecimal.ZERO;

  public Sumary(BigDecimal totalDue, BigDecimal amountPaid) {
    this.totalDue = totalDue;
    this.amountPaid = amountPaid;
    this.remaining = totalDue.subtract(amountPaid);
  }

  public BigDecimal getTotalDue() {
    return totalDue.setScale(2, RoundingMode.HALF_UP);
  }

  public void setTotalDue(BigDecimal totalDue) {
    this.totalDue = totalDue;
  }

  public BigDecimal getAmountPaid() {
    return amountPaid.setScale(2, RoundingMode.HALF_UP);
  }

  public void setAmountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
  }

  public BigDecimal getRemaining() {
    return remaining.setScale(2, RoundingMode.HALF_UP);
  }

  public void setRemaining(BigDecimal remaining) {
    this.remaining = remaining;
  }

}
