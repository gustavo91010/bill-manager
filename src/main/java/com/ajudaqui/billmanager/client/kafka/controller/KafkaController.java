package com.ajudaqui.billmanager.client.kafka.controller;

import java.util.Map;

import com.ajudaqui.billmanager.client.kafka.service.KafkaProducer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

  private final KafkaProducer kafkaProducer;

  public KafkaController(KafkaProducer kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
  }

  @PostMapping("/send")
  public ResponseEntity<Map<String, String>> sendMessage(@RequestParam String topic, @RequestParam String message) {

    kafkaProducer.sendMessage(topic, message);
    return ResponseEntity.ok(Map.of("message", "Enviada com sucesso!"));
  }

}
