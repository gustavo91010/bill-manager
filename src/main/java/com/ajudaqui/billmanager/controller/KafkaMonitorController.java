
package com.ajudaqui.billmanager.controller;

import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.ajudaqui.billmanager.service.KafkaMonitorService;

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

