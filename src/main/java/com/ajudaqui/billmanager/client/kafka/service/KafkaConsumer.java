package com.ajudaqui.billmanager.client.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "novo-topico", groupId = "bill-manager")
  public void listen(String message) {
    System.out.println("Mensagem recebida: " + message);
  }

}
