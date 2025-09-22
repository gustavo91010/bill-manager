package com.ajudaqui.billmanager.client.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "test", groupId = "bill-manager")
  public void listen(String message) {
    System.out.println("Mensagem recebida: " + message);
  }

}
