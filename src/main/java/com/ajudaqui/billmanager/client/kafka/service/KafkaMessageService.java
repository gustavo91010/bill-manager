package com.ajudaqui.billmanager.client.kafka.service;

import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ajudaqui.billmanager.client.kafka.entity.KafkaMessage;

import org.springframework.stereotype.Service;

@Service
public class KafkaMessageService {

  @Transactional
  public KafkaMessage factor(String topic, Map<String, Object> message) {

    if (message.get("accessToken") == null)
      message.put("accessToken", UUID.randomUUID().toString());
    return new KafkaMessage(topic, message.get("accessToken").toString(), message);
  }

}
