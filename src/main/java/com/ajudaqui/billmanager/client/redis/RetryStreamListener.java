package com.ajudaqui.billmanager.client.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetryStreamListener implements StreamListener<String, ObjectRecord<String, String>> {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;
  private RetryService retryService;
  private ObjectMapper objectMapper;

  @Override
  public void onMessage(ObjectRecord<String, String> message) {
    try {
      String mensagemJson = message.getValue();
      FailedMessage failedMessage = objectMapper.readValue(mensagemJson, FailedMessage.class);
https://chat.deepseek.com/a/chat/s/52ed88fc-7d8c-425c-b1ef-5a315df3fa07
      // Verifica se já é hora do retry
      if (System.currentTimeMillis() >= failedMessage.getRetryTime()) {
        // Reenvia para o Kafka
        kafkaTemplate.send(failedMessage.getOriginalTopic(), failedMessage.getPayload());

        // Log de sucesso
        System.out.println("Retry realizado para mensagem: " + failedMessage.getId());
      } else {
        // Se ainda não é hora, recoloca no stream com novo delay
        recolocarComDelay(failedMessage);
      }

    } catch (Exception e) {
      System.err.println("Erro no retry: " + e.getMessage());
    }
  }

  private void recolocarComDelay(FailedMessage failedMessage) {
    // Recalcula delay (backoff exponencial)
    long novoDelay = Math.min(failedMessage.getDelay() * 2, 3600000); // Max 1 hora
    failedMessage.setDelay(novoDelay);
    failedMessage.setRetryTime(System.currentTimeMillis() + novoDelay);

    // Recoloca no stream
    try {
      retryService.salvarNoRedisStream(failedMessage);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
