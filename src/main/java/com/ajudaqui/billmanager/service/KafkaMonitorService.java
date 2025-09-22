package com.ajudaqui.billmanager.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.ajudaqui.billmanager.exception.MsgException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaMonitorService {

  private AdminClient admin;
  @Value("$spring.kafka.bootstrap-servers")
  private String kafkaServer;

  public Map<String, Object> getClusterInfo() {
    DescribeClusterResult result = admin.describeCluster();

    Map<String, Object> data = new HashMap<>();
    try {
      data.put("clusterId", result.clusterId().get());
      data.put("controller", result.controller().get().idString());
      data.put("nodes", result.nodes().get().toString());

    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na consulta dos topicos: " + e.getMessage());
    }
    return data;
  }
}

// @GetMapping("/topics")
// public Map<String, Object> getTopicsInfo() throws ExecutionException,
// InterruptedException {
// Map<String, Object> response = new HashMap<>();

// Set<String> topics = admin.listTopics().names().get();
// response.put("topics", topics);

// Map<String, Object> details = new HashMap<>();
// DescribeTopicsResult desc = admin.describeTopics(topics);

// desc.all().get().forEach((name, t) -> {
// Map<String, Object> info = new HashMap<>();
// // particao é onde as mensagens ficam
// info.put("partitions", t.partitions().size());
// info.put("replicationFactor", t.partitions().get(0).replicas().size());
// details.put(name, info);

// // TopicPartition tp= new TopicPartition(name, t.partitions());
// });

// response.put("details", details);
// return response;
// }
// }

// // desc.all().get().forEach((name, t) -> {
// // Map<String, Object> info = new HashMap<>();
// // info.put("partitions", t.partitions().size());
// // info.put("replicationFactor", t.partitions().get(0).replicas().size());

// // long totalMessages = 0;
// // for (var p : t.partitions()) {
// // TopicPartition tp = new TopicPartition(name, p.partition());
// // ListOffsetsResult lor = admin.listOffsets(Map.of(tp,
// OffsetSpec.latest()));
// // long lastOffset = lor.all().get().get(tp).offset();
// // totalMessages += lastOffset;
// // }

// // info.put("totalMessages", totalMessages);
// // details.put(name, info);
// // });

// }
