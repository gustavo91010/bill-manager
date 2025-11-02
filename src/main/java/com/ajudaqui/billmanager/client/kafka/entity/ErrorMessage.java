package com.ajudaqui.billmanager.client.kafka.entity;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.*;

public class ErrorMessage {

  private String topic;
  private String accessToken;

  private Map<String, Object> message;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public ErrorMessage(String topic, String accessToken, Map<String, Object> metadata) {
    this.topic = topic;
    this.accessToken = accessToken;
    this.message = metadata;
    this.createdAt = LocalDateTime.now();
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Map<String, Object> getMessage() {
    return message;
  }

  public void setMessage(Map<String, Object> metadata) {
    this.message = metadata;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

}
