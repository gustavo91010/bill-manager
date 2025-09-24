package com.ajudaqui.billmanager.client.kafka.service;

import com.ajudaqui.billmanager.client.kafka.controller.KafkaController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

@Service
public class KafkaProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
  private static final Logger log = LoggerFactory.getLogger(KafkaController.class);

  public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
      Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
    this.kafkaTemplate = kafkaTemplate;
    this.circuitBreakerFactory = circuitBreakerFactory;
  }

  public void sendMessage(String topic, String message) {
    Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafka-producer");

    circuitBreaker.run(() -> {
      // Tenta enviar para o kafka
      return this.kafkaTemplate.send(topic, message);

    }, throwable -> {
      return handleKafkaFallback(topic, message, throwable.getMessage());
    });

  }

  private ListenableFuture<SendResult<String, String>> handleKafkaFallback(String topic, String message,
      String error) {
    log.error("Erro ao enviar mensagem para Kafka [topic={}]: {}", topic, error);

    return null;
  }

}
