package com.ajudaqui.billmanager.client.kafka.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.ajudaqui.billmanager.client.kafka.controller.KafkaController;
import com.ajudaqui.billmanager.client.kafka.entity.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;

@Service
public class KafkaProducer {
  private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
  private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
  private final ErrorMessageService errorService;
  private final CircuitBreakerRegistry circuitBreakerRegistry;

  private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
  private volatile boolean isKafkaAvailable = true;
  private CircuitBreaker kafkaCircuitBreaker;

  public KafkaProducer(KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
      Resilience4JCircuitBreakerFactory circuitBreakerFactory, ErrorMessageService errorService,
      CircuitBreakerRegistry circuitBreakerRegistry) {
    this.kafkaTemplate = kafkaTemplate;
    this.circuitBreakerFactory = circuitBreakerFactory;
    this.errorService = errorService;
    this.circuitBreakerRegistry = circuitBreakerRegistry;
    initializeCircuitBreaker();
  }

  private void initializeCircuitBreaker() {
    // Obtém o Circuit Breaker pelo nome registrado na configuração
    this.kafkaCircuitBreaker = circuitBreakerRegistry.circuitBreaker("kafka-producer");

    // Registra os listeners para eventos do Circuit Breaker
    kafkaCircuitBreaker.getEventPublisher()
        .onStateTransition(this::handleStateTransition)
        .onSuccess(event -> log.debug("Circuit Breaker success event"))
        .onError(event -> log.debug("Circuit Breaker error event"));

    log.info("Circuit Breaker initialized with state: {}", kafkaCircuitBreaker.getState());
    isKafkaAvailable = kafkaCircuitBreaker.getState() == CircuitBreaker.State.CLOSED;
  }

  private void handleStateTransition(CircuitBreakerOnStateTransitionEvent event) {
    CircuitBreaker.State newState = event.getStateTransition().getToState();
    log.info("Circuit Breaker state changed to: {}", newState);

    switch (newState) {
      case CLOSED:
        handleKafkaRecovery();
        isKafkaAvailable = true;
        break;
      case OPEN:
        isKafkaAvailable = false;
        log.warn("Kafka is unavailable. Messages will be stored temporarily.");
        break;
      case HALF_OPEN:
        isKafkaAvailable = false;
        log.info("Testing Kafka connection...");
        break;
    }
  }

  private void handleKafkaRecovery() {
    log.info("Kafka is available again. Attempting to resend stored messages...");

    try {
      List<ErrorMessage> storedMessages = errorService.findAll();

      if (storedMessages.isEmpty()) {
        log.info("No stored messages to resend.");
        return;
      }

      log.info("Found {} messages to resend.", storedMessages.size());
      int successCount = 0;
      int errorCount = 0;

      for (ErrorMessage errorMessage : storedMessages) {
        try {
          // Tenta reenviar a mensagem diretamente sem usar o Circuit Breaker
          kafkaTemplate.send(errorMessage.getTopic(), errorMessage.getMessage());
          errorService.delete(errorMessage.getId());
          successCount++;

          // Pequena pausa para não sobrecarregar
          Thread.sleep(50);
        } catch (Exception e) {
          log.error("Failed to resend message [id={}, topic={}]: {}",
              errorMessage.getId(), errorMessage.getTopic(), e.getMessage());
          errorCount++;

          // Se houver erro consecutivo, para o reenvio
          if (errorCount >= 3) {
            log.warn("Too many errors during recovery. Stopping resend process.");
            break;
          }
        }
      }

      log.info("Message recovery completed. Success: {}, Errors: {}", successCount, errorCount);

    } catch (Exception e) {
      log.error("Error during message recovery process: {}", e.getMessage());
    }
  }

  // Método para verificar o estado atual do Circuit Breaker
  public CircuitBreaker.State getCircuitBreakerState() {
    return kafkaCircuitBreaker != null ? kafkaCircuitBreaker.getState() : CircuitBreaker.State.CLOSED;
  }

  // Agendado para verificar mensagens pendentes periodicamente
  @Scheduled(fixedDelay = 30000)
  public void scheduledMessageRecovery() {
    if (isKafkaAvailable && kafkaCircuitBreaker.getState() == CircuitBreaker.State.CLOSED) {
      long pendingCount = errorService.count();
      if (pendingCount > 0) {
        log.info("Scheduled recovery found {} pending messages", pendingCount);
        handleKafkaRecovery();
      }
    }
  }

  // Método para forçar a verificação de recuperação (útil para testes)
  public void triggerManualRecovery() {
    if (kafkaCircuitBreaker.getState() == CircuitBreaker.State.CLOSED) {
      handleKafkaRecovery();
    }
  }

  public void sendMessage(String topic, Map<String, Object> message) {
    Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafka-producer");

    circuitBreaker.run(() -> {
      kafkaTemplate.send(topic, message);
      // errorService.create(topic, message);
      return true;
    }, throwable -> {
      handleKafkaFallback(topic, message, throwable.getMessage());
        // TODO nao ta entrando no fallback
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
