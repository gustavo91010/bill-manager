package com.ajudaqui.billmanager.client.kafka.entity;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Table
@Entity(name = "error_message")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ErrorMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "topic", nullable = false)
  private String topic;
  @Column(name = "access_token", nullable = false)
  private String accessToken;

  @Type(type = "jsonb")
  @Column(name = "metadata", columnDefinition = "jsonb")
  private Map<String, Object> message;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public ErrorMessage(String topic, String accessToken, Map<String, Object> metadata) {
    this.topic = topic;
    this.accessToken = accessToken;
    this.message = metadata;
    this.createdAt = LocalDateTime.now();
  }

  @Override
  public String toString() {
    return "ErrorMessage{id=" + id + ", topic=" + topic + ", accessToken=" + accessToken + "}";
  }

  public ErrorMessage() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
