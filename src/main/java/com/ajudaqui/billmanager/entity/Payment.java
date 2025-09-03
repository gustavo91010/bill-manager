package com.ajudaqui.billmanager.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "users_id")
  private Users user;
  private String description;
  private BigDecimal value;
  @Column(name="due_date")
  private LocalDate dueDate;
  @Column(name="created_at")
  private LocalDateTime createdAt;
  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  private StatusBoleto status;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Override
  public String toString() {
    return "Payment{id=" + id + ", description=" + description + ", value=" + value + ", dueDate=" + dueDate
        + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", status=" + status + ", category=" + category.getName()
        + "}";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public StatusBoleto getStatus() {
    return status;
  }

  public void setStatus(StatusBoleto status) {
    this.status = status;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Payment other = (Payment) obj;
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    if (dueDate == null) {
      if (other.dueDate != null) {
        return false;
      }
    } else if (!dueDate.equals(other.dueDate)) {
      return false;
    }
    return true;
  }
}
