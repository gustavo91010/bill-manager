package com.ajudaqui.billmanager.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank(message = "O nome não pode ser vazio")
  private String name;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users users;
  @JsonIgnore
  // @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  @OneToMany(mappedBy = "category")
  private Set<Payment> payments = new HashSet<>();

  public Category(String name, Users users) {
    this.name = name;
    this.users = users;
  }

  public Category(String name) {
    this.name = name;
  }

  public Category() {
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

  public Users getUsers() {
    return users;
  }

  public void setUsers(Users users) {
    this.users = users;
  }

  public Set<Payment> getPayments() {
    return payments;
  }

  public void setPayments(Set<Payment> payments) {
    this.payments = payments;
  }

}
