package com.ajudaqui.billmanager.client.kafka.service;

import java.util.Map;
import java.util.concurrent.*;

import javax.annotation.PostConstruct;

import com.ajudaqui.billmanager.client.kafka.entity.KafkaMessage;
import com.ajudaqui.billmanager.client.redis.RetryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
  @Autowired
  private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
  @Autowired
  private Resilience4JCircuitBreakerFactory circuitBreakerFactory;
  @Autowired
  private KafkaMessageService errorService;
  @Autowired
  private RetryService retryService;

  private BlockingQueue<KafkaMessage> fila;
  private ExecutorService executor;
  private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

  @PostConstruct
  public void init() {
    if (fila == null) {
      fila = new LinkedTransferQueue<>();
    }

    // 3 threads para processar a fila
    executor = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 3; i++) {
      executor.submit(this::processarFila);
    }
  }

  public void sendMessage(String topic, Map<String, Object> message) {
    KafkaMessage messageSaved = errorService.factor(topic, message);
    fila.offer(messageSaved);
    System.out.println("Mensagem adicionada na fila: " + messageSaved.getAccessToken());
  }

  private void processarFila() {

    Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafka-producer");

    while (true) {
      try {
        KafkaMessage message = fila.take(); // ele espera ate ter um elemento na fila
        circuitBreaker.run(() -> {
          try {
            kafkaTemplate.send(message.getTopic(), message.getMessage()).get();
          } catch (InterruptedException | ExecutionException e) {
            e.getMessage();
          }
          return true;
        }, throwable -> {
          handleKafkaFallback(message.getTopic(), throwable.getMessage(), message);
          return false;
        });

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  private void handleKafkaFallback(String topic, String error, KafkaMessage message) {
    log.error("Erro ao enviar mensagem para Kafka [topic={}]: {}", topic, error);
    retryService.salvarMessage(message);
  }

  public void reenviarMensagens() {
    fila.addAll(retryService.getFailedMessagens());

  }
}
