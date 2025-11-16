package com.ajudaqui.billmanager.config.circuitBraker;

import javax.annotation.PostConstruct;

import com.ajudaqui.billmanager.utils.ClientName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;

@Component
public class CircuitBreakerMonitor {

  @Autowired
  private Resilience4JCircuitBreakerFactory factory;

  @Autowired
  private com.ajudaqui.billmanager.client.redis.RetryService RetryService;

  @PostConstruct
  public void init() {
    // recupera o CircuitBreakerRegistry interno da fábrica
    var registry = factory.getCircuitBreakerRegistry();

    for (ClientName client : ClientName.values()) {

      CircuitBreaker breaker = registry.circuitBreaker(client.name());

      breaker.getEventPublisher().onStateTransition(
          event -> handleTransition(event, client.name()));
    }
  }

  private void handleTransition(CircuitBreakerOnStateTransitionEvent event, String ClientName) {
    if (event.getStateTransition().getToState().name().equals("CLOSED")) {
      // KafkaProducer.reenviarMensagens();
      System.out
          .println("✅ Circuito para client " + ClientName + " fechado novamente, reenviando mensagens pendentes...");
    }
  }
}
