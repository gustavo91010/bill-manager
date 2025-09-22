package com.ajudaqui.billmanager.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

  public NewTopic newTopic() {
    return TopicBuilder.name("test")
        .partitions(1)
        .replicas(1)
        .build();
  }

}
