
package com.ajudaqui.billmanager.client.kafka.controller;

import com.ajudaqui.billmanager.client.kafka.service.KafkaMonitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaMonitorController {

  @Autowired
  private  KafkaMonitorService kafkaServer;

  @GetMapping("/cluster")
  public ResponseEntity<?> getClusterInfo() {
    return ResponseEntity.ok(kafkaServer.getClusterInfo());
  }

  @GetMapping("/topics")
  public ResponseEntity<?> getTopicsInfo() {
    return ResponseEntity.ok(kafkaServer.getTopicsInfo());
  }
}

