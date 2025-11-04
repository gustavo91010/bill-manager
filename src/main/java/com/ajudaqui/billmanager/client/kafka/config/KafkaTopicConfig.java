package com.ajudaqui.billmanager.client.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic newTopic() {
    return TopicBuilder.name("test")
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic attPayment() {
    return TopicBuilder.name("att-payment")
        .partitions(1)
        .replicas(1)
        .build();
  }

}
