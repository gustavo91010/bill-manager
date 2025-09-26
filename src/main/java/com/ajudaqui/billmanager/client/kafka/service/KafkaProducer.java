package com.ajudaqui.billmanager.client.kafka.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
  private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
  private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
  private final ErrorMessageService errorService;

  private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

  public KafkaProducer(KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
      Resilience4JCircuitBreakerFactory circuitBreakerFactory, ErrorMessageService errorService) {
    this.kafkaTemplate = kafkaTemplate;
    this.circuitBreakerFactory = circuitBreakerFactory;
    this.errorService = errorService;
  }

  public void sendMessage(String topic, Map<String, Object> message) {
    Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafka-producer");

    circuitBreaker.run(() -> {
      kafkaTemplate.send(topic, message);
      return true;
    }, throwable -> {
      handleKafkaFallback(topic, message, throwable.getMessage());
      return false;
    });
  }

  private void handleKafkaFallback(String topic, Map<String, Object> message,
      String error) {
    sendErrorMessage();
    log.error("Erro ao enviar mensagem para Kafka [topic={}]: {}", topic, error);
    errorService.create(topic, message);
  }

  private void sendErrorMessage() {
    // Resilience4JCircuitBreaker circuitBreaker =
    // circuitBreakerFactory.create("kafka-producer");
    // System.out.println("status " + this.kafkaCircuitBreaker.getState());
    // System.out.println("name " + this.kafkaCircuitBreaker.getName());
    // System.out.println("tags " + this.kafkaCircuitBreaker.getTags());
  }
}
