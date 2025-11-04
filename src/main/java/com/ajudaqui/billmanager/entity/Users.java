package com.ajudaqui.billmanager.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean active;

  @Column(name = "access_token", nullable = false)
  private String accessToken;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Payment> payments = new ArrayList<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private Set<Category> categories = new HashSet<>();
  @Column(name = "is_cal_control")
  public boolean isCalControl;

  public Users(String accessToken) {
    this.accessToken = accessToken;
    this.active = true;
  }

  public Users() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public List<Payment> getPayments() {
    return payments;
  }

  public void setPayments(List<Payment> payments) {
    this.payments = payments;
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  public boolean isCalControl() {
    return isCalControl;
  }

  public void setCalControl(boolean isCalControl) {
    this.isCalControl = isCalControl;
  }

}
