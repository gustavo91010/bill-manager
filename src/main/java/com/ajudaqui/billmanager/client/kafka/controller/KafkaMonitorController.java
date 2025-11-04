
package com.ajudaqui.billmanager.client.kafka.controller;

import com.ajudaqui.billmanager.client.kafka.service.KafkaMonitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaMonitorController {

  @Autowired
  private KafkaMonitorService kafkaServer;

  @GetMapping("/cluster")
  public ResponseEntity<?> getClusterInfo(@RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(kafkaServer.getClusterInfo(accessToken));
  }

  @GetMapping("/all-topics")
  public ResponseEntity<?> getTopicsInfo(@RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(kafkaServer.getTopicsInfo(accessToken, accessToken));
  }

  @GetMapping("/info-topics/{name}")
  public ResponseEntity<?> prooffset(@RequestHeader("Authorization") String accessToken, @PathVariable String name) {
    return ResponseEntity.ok(kafkaServer.prooffset(accessToken, name));
  }

  @GetMapping("/allConsumers")
  public ResponseEntity<?> allConsumers(@RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(kafkaServer.allConsumers(accessToken));
  }

  @PostMapping("/topics/create")
  public ResponseEntity<?> create(
      @RequestHeader("Authorization") String accessToken,
      @RequestParam(required = true) String name,
      @RequestParam(required = false, defaultValue = "1") int numPartitions,
      @RequestParam(required = false, defaultValue = "1") short replicationFactor) {
    return ResponseEntity.ok(kafkaServer.criarTopico(accessToken, name, numPartitions, replicationFactor));
  }

  @DeleteMapping("/topics/delete/{name}")
  public ResponseEntity<?> delete(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable String name) {
    return ResponseEntity.ok(kafkaServer.deleteTopico(name, accessToken));
  }

}
