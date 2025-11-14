// package com.ajudaqui.billmanager.client.redis;

// import java.util.ArrayList;
// import java.util.List;

// import com.ajudaqui.billmanager.client.kafka.entity.KafkaMessage;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Service;

// @Service
// public class RetryService {

//   @Autowired
//   private RedisTemplate<String, String> redisTemplate;
//   @Autowired
//   private ObjectMapper objectMapper;

//   public void salvarMessage(KafkaMessage message) {
//     try {
//       String value = objectMapper.writeValueAsString(message);
//       redisTemplate.opsForList().rightPush("failed-messages", value);
//     } catch (JsonProcessingException e) {
//       e.printStackTrace();
//     }
//   }

//   public List<KafkaMessage> getFailedMessagens() {
//     List<String> values = redisTemplate.opsForList().range("failed-messages", 0, -1);
//     List<KafkaMessage> messages = new ArrayList<>();
//     if (values != null) {
//       for (String message : values) {
//         try {
//           messages.add(objectMapper.readValue(message, KafkaMessage.class));
//         } catch (JsonProcessingException e) {
//           e.printStackTrace();
//         }

//       }
//     }
//     return messages;

//   }

//   public void limparFailedMessages() {
//     redisTemplate.delete("failed-messages");
//   }

// }
