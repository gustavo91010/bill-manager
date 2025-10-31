package com.ajudaqui.billmanager.config.circuitBraker;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;

@Component
public class CircuitBreakerMonitor {

  @Autowired
  private Resilience4JCircuitBreakerFactory factory;

  @PostConstruct
  public void init() {
    // recupera o CircuitBreakerRegistry interno da fábrica
    var registry = factory.getCircuitBreakerRegistry();
    CircuitBreaker breaker = registry.circuitBreaker("kafka-producer");

    breaker.getEventPublisher().onStateTransition(this::handleTransition);
  }

  private void handleTransition(CircuitBreakerOnStateTransitionEvent event) {
    if (event.getStateTransition().getToState().name().equals("CLOSED")) {
      System.out.println("✅ Circuito kafka-producer fechado novamente, reenviando mensagens pendentes...");
      // retryService.resendFailedMessages();
    }
  }
}
