package com.ajudaqui.billmanager.controller.from;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BoletoFrom {
  private String description;
  private BigDecimal value;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate due_date;
  private String status;

  public Payment convert() {
    Payment payament = new Payment();
    payament.setDescription(this.description);
    payament.setValue(this.value);
    payament.setDueDate(this.due_date);
    payament.setStatus(StatusBoleto.valueOf(status));

    return payament;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public LocalDate getDueDate() {
    return due_date;
  }

  public void setDueDate(LocalDate dueDate) {
    this.due_date = dueDate;
  }

  public String getStatus() {
    return status;
  }

}
