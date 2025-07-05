package com.ajudaqui.billmanager.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Boolean active;
  private String accessToken;
  @OneToMany
  private List<Payment> payaments = new ArrayList<>();

  public Users() {
    this.active = true;
    this.accessToken = UUID.randomUUID().toString();
  }

  public Users(String accessToken) {
    this.accessToken = accessToken;
    this.active = true;
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

  public List<Payment> getPayaments() {
    return payaments;
  }

  public void setPayaments(List<Payment> payaments) {
    this.payaments = payaments;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

}
