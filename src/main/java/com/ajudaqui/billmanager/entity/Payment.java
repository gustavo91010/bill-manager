package com.ajudaqui.billmanager.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
  private LocalDate due_date;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;

  @Enumerated(EnumType.STRING)
  private StatusBoleto status;

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

  public LocalDate getDue_date() {
    return due_date;
  }

  public void setDue_date(LocalDate due_date) {
    this.due_date = due_date;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    result = prime * result + ((due_date == null) ? 0 : due_date.hashCode());
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
    if (user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!user.equals(other.user)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    if (due_date == null) {
      if (other.due_date != null) {
        return false;
      }
    } else if (!due_date.equals(other.due_date)) {
      return false;
    }
    return true;
  }

}
