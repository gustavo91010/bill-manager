package com.ajudaqui.billmanager.client.kafka.service;

import java.util.Map;

import com.ajudaqui.billmanager.client.kafka.entity.ErrorMessage;

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
    ErrorMessage messageSaved = errorService.create(topic, message);
    messageSaved.getMessage().put("message_id", messageSaved.getId());
    System.out.println("salvando a mensagem de id " + messageSaved.getId());
    Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafka-producer");

    Boolean sent = circuitBreaker.run(() -> {
      kafkaTemplate.send(topic, message);
      return true;
    }, throwable -> {
      handleKafkaFallback(topic, throwable.getMessage());
      return false;
    });
    System.out.println("sent " + sent);
    if (sent) {
      errorService.delete(messageSaved.getId());
    }
  }

  private void handleKafkaFallback(String topic, String error) {
    log.error("Erro ao enviar mensagem para Kafka [topic={}]: {}", topic, error);
  }

}
