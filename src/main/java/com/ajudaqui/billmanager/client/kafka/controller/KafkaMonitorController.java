
package com.ajudaqui.billmanager.client.kafka.controller;

import com.ajudaqui.billmanager.client.kafka.service.KafkaMonitorService;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaMonitorController {

  @Autowired
  private KafkaMonitorService kafkaServer;

  @GetMapping("/cluster")
  public ResponseEntity<?> getClusterInfo() {
    return ResponseEntity.ok(kafkaServer.getClusterInfo());
  }

  @GetMapping("/topics")
  public ResponseEntity<?> getTopicsInfo() {
    return ResponseEntity.ok(kafkaServer.getTopicsInfo());
  }

  @GetMapping("/prooffset/{name}")
  public ResponseEntity<?> prooffset(@PathVariable String name) {
    return ResponseEntity.ok(kafkaServer.prooffset(name));
  }

  @GetMapping("/allConsumers")
  public ResponseEntity<?> allConsumers() {
    return ResponseEntity.ok(kafkaServer.allConsumers());
  }

  @PostMapping("/topics/create")
  public ResponseEntity<?> create(
      @RequestParam(required = true) String name,
      @RequestParam(required = false, defaultValue = "1") int numPartitions,
      @RequestParam(required = false, defaultValue = "1") short replicationFactor) {
    return ResponseEntity.ok(kafkaServer.criarTopico(name, numPartitions, replicationFactor));
  }

  @DeleteMapping("/topics/delete/{name}")
  public ResponseEntity<?> delete(
      @PathVariable String name) {
    return ResponseEntity.ok(kafkaServer.deleteTopico(name));
  }

}
