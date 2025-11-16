package com.ajudaqui.billmanager.client.redis;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private ObjectMapper objectMapper;

  public void salvarMessage(FailedMessage message, String clientName) {
    try {
      String value = objectMapper.writeValueAsString(message);
      redisTemplate.opsForList().rightPush(clientName, value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public List<FailedMessage> getFailedMessagens(String clientName) {
    List<String> values = redisTemplate.opsForList().range(clientName, 0, -1);
    List<FailedMessage> messages = new ArrayList<>();
    if (values != null) {
      for (String message : values) {
        try {
          messages.add(objectMapper.readValue(message, FailedMessage.class));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }

      }
    }
    return messages;

  }

  public void limparFailedMessages(String clientName) {
    redisTemplate.delete(clientName);
  }

}
