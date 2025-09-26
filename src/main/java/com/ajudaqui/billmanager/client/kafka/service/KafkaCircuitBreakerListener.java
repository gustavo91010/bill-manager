package com.ajudaqui.billmanager.client.kafka.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Service
public class KafkaCircuitBreakerListener {

  private final CircuitBreakerRegistry rCircuitBreaker;
  private final ErrorMessageService eMessageService;

  public KafkaCircuitBreakerListener(CircuitBreakerRegistry rCircuitBreaker, ErrorMessageService eMessageService) {
    this.rCircuitBreaker = rCircuitBreaker;
    this.eMessageService = eMessageService;
  }

  @PostConstruct
  public void registerListner() {
    CircuitBreaker breaker = rCircuitBreaker.circuitBreaker("kafka-producer");

    breaker.getEventPublisher()
        .onStateTransition(event -> {
          System.out.println();
          System.out.println("llllaaaaaaaaaaaaaaaaa");
          System.out.println("getStateTransition " + event.getStateTransition());
          System.out.println();

          if (event.getStateTransition() == CircuitBreaker.StateTransition.HALF_OPEN_TO_CLOSED) {
            eMessageService.sendPendentMessages();
          }
        });
  }

}
