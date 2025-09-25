package com.ajudaqui.billmanager.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CircuitBrakerConfig {

  @Bean
  public Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerCustomizer() {

    return factory -> {
      // Configuração para chamada de api
      factory.configure(Builder -> Builder
          .circuitBreakerConfig(CircuitBreakerConfig.custom()
              .failureRateThreshold(50) // porcentagem de falhas em chamada no periodo de tempo validado
              .waitDurationInOpenState(Duration.ofSeconds(30)) // tempo que o circuito fica aberto antes de tentar
                                                               // fechar novamente
              .slidingWindowSize(10) // tempo de validacao de falha
              .build()) // tipo, vai ficar aberto por 10s se tiver falha de 50% das request ele abre...
          .timeLimiterConfig(TimeLimiterConfig.custom()
              .timeoutDuration(Duration.ofSeconds(5)) // tempo de espera de timeout para abir o circuito
              .build()),
          "http-call");

      // configurado para o producer do kafka
      factory.configure(builder -> builder
          .circuitBreakerConfig(CircuitBreakerConfig.custom()
              .failureRateThreshold(60)
              .waitDurationInOpenState(Duration.ofSeconds(60))
              .slidingWindowSize(5)
              .enableAutomaticTransitionFromOpenToHalfOpen() // habilita o evento de tarnsição de estado
              .build())
          .timeLimiterConfig(TimeLimiterConfig.custom()
              .timeoutDuration(Duration.ofSeconds(5))
              .build()),
          "kafka-producer");
    };
  }

}
