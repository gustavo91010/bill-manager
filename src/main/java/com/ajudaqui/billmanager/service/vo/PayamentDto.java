package com.ajudaqui.billmanager.service.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.ValidarStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PayamentDto {
  @NotBlank(message = "O campo descrição é obrigatorio")
  private String description;
  @NotNull(message = "O campo valor é obrigatório")
  private BigDecimal value;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate due_date;
  private String category;
  private StatusBoleto status;

  public PayamentDto() {
  }

  public PayamentDto(Payment payaments) {
    this.description = payaments.getDescription();
    this.value = payaments.getValue();
    this.due_date = payaments.getDueDate();
    this.status = payaments.getStatus();
  }

  public Payment toDatabase(Users users) {
    Payment payament = new Payment();
    payament.setUser(users);
    payament.setDescription(this.description);
    payament.setValue(this.value);
    payament.setCreatedAt(LocalDateTime.now());
    payament.setUpdatedAt(LocalDateTime.now());

    payament.setDueDate(this.due_date);
    ValidarStatus.statusAtualizado(payament);
    return payament;
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
    this.value = value.setScale(2, RoundingMode.HALF_UP);
  }

  public LocalDate getDue_date() {
    return due_date;
  }

  public void setDue_date(LocalDate dueDate) {
    this.due_date = dueDate;
  }

  public StatusBoleto getStatus() {
    return status;
  }

  public void setStatus(StatusBoleto status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "PayamentDto [description=" + description + ", value=" + value + ", due_date=" + due_date + ", status="
        + status + "]";
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

}
