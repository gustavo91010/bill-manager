package com.ajudaqui.billmanager.response;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public class ApiSumaryCategory {

  private BigDecimal total;
  private Map<String, BigDecimal> sumary;

  public ApiSumaryCategory(Map<String, BigDecimal> sumary) {
    this.total = sumaryTotal(sumary.values());
    this.sumary = sumary;
  }

  private BigDecimal sumaryTotal(Collection<BigDecimal> values) {
    return values.stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public ApiSumaryCategory() {
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Map<String, BigDecimal> getSumary() {
    return sumary;
  }

  public void setSumary(Map<String, BigDecimal> sumary) {
    this.sumary = sumary;
  }

}
