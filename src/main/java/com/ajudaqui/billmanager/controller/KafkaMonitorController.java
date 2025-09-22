
package com.ajudaqui.billmanager.controller;

import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/kafka")
public class KafkaMonitorController {

  private final AdminClient admin;
  // @Value("$spring.kafka.bootstrap-servers")
  // private String kafkaServer;

  // @Value("${info.app.environment}")
      public KafkaMonitorController(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
  // public KafkaMonitorController() {
    Properties props = new Properties();
    // props.put("bootstrap.servers", "localhost:9092");
    props.put("bootstrap.servers",kafkaServer);
    this.admin = AdminClient.create(props);
  }

  @GetMapping("/cluster")
  public Map<String, Object> getClusterInfo() throws ExecutionException, InterruptedException {
    DescribeClusterResult result = admin.describeCluster();

    Map<String, Object> data = new HashMap<>();
    data.put("clusterId", result.clusterId().get());
    data.put("controller", result.controller().get().idString());
    data.put("nodes", result.nodes().get().toString());

    return data;
  }

  @GetMapping("/topics")
  public Map<String, Object> getTopicsInfo() throws ExecutionException, InterruptedException {
    Map<String, Object> response = new HashMap<>();

    Set<String> topics = admin.listTopics().names().get();
    response.put("topics", topics);

    Map<String, Object> details = new HashMap<>();
    DescribeTopicsResult desc = admin.describeTopics(topics);

    desc.all().get().forEach((name, t) -> {
      Map<String, Object> info = new HashMap<>();
      // particao é onde as mensagens ficam
      info.put("partitions", t.partitions().size());
      info.put("replicationFactor", t.partitions().get(0).replicas().size());
      details.put(name, info);


      // TopicPartition tp= new TopicPartition(name, t.partitions());
    });

    response.put("details", details);
    return response;
  }
}

// desc.all().get().forEach((name, t) -> {
//     Map<String, Object> info = new HashMap<>();
//     info.put("partitions", t.partitions().size());
//     info.put("replicationFactor", t.partitions().get(0).replicas().size());

//     long totalMessages = 0;
//     for (var p : t.partitions()) {
//         TopicPartition tp = new TopicPartition(name, p.partition());
//         ListOffsetsResult lor = admin.listOffsets(Map.of(tp, OffsetSpec.latest()));
//         long lastOffset = lor.all().get().get(tp).offset();
//         totalMessages += lastOffset;
//     }

//     info.put("totalMessages", totalMessages);
//     details.put(name, info);
// });

