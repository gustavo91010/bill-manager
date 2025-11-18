package com.ajudaqui.billmanager.client.model;

import java.util.Map;

public class MessageCalControl {

  private String email;
  private String day;
  private Map<String, String> payload;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public Map<String, String> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, String> payload) {
    this.payload = payload;
  }

  // @field:Email(message = "E-mail inválido") val email: String,
  // @field:Pattern(
  // regexp = "\\d{4}-\\d{2}-\\d{2}",
  // message = "O campo day deve estar no formato yyyy-MM-dd",
  // )
  // val day: String,
  // val payload: Map<String, String>,
}
