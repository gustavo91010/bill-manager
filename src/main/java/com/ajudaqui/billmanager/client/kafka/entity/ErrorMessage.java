package com.ajudaqui.billmanager.client.kafka.entity;

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

  @Type(type = "json")
  @Column(name = "metadata", columnDefinition = "jsonb")
  private String metadata;
}
