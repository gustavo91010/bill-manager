package com.ajudaqui.billmanager.client.kafka.controller;

import java.util.HashMap;
import java.util.Map;

import com.ajudaqui.billmanager.client.kafka.service.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

  @Autowired
  private KafkaProducer kafkaProducer;

  @PostMapping("/send")
  public ResponseEntity<Map<String, String>> sendMessage(@RequestParam String topic, @RequestParam String message) {
    Map<String, Object> lalala = new HashMap<>();
    lalala.put("message", message);
    kafkaProducer.sendMessage(topic, lalala);
    return ResponseEntity.ok(Map.of("message", "Enviada com sucesso!"));
  }


}
