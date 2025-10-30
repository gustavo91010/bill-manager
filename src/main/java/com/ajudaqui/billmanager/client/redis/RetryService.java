package com.ajudaqui.billmanager.client.redis;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;
  @Autowired
  private ObjectMapper objectMapper;

  public void salvarParaRetry(String topic, Object payload, String error) {
    FailedMessage failedMessage = new FailedMessage(
        UUID.randomUUID().toString(),
        topic,
        payload,
        System.currentTimeMillis() + 5000, // Primeiro retry em 5s
        5000, // Delay inicial
        1,
        error);

    try {
      salvarNoRedisStream(failedMessage);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void salvarNoRedisStream(FailedMessage message) throws JsonProcessingException {
    ObjectRecord<String, String> record = ObjectRecord.create(
        "retry-stream",
        objectMapper.writeValueAsString(message));

    redisTemplate.opsForStream().add(record);
  }
}
