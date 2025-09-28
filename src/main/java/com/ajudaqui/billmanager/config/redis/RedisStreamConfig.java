package com.ajudaqui.billmanager.config.redis;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.stream.*;

import org.springframework.data.redis.stream.StreamListener;
@Configuration
public class RedisStreamConfig {

  // Cria e configura o container que vai escutar mensagens do Redis Stream
  @Bean
  public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamContainer(
      RedisConnectionFactory connectionFactory) {

    // Define as opções do container:
    // - pollTimeout: tempo máximo de espera entre polls no stream
    // - targetType: tipo do payload que será recebido (String aqui)
    StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
        .builder()
        .pollTimeout(Duration.ofSeconds(1))
        .targetType(String.class)
        .build();

    // Cria o container com a conexão Redis e as opções configuradas
    return StreamMessageListenerContainer.create(connectionFactory, options);
  }

  // Registra a assinatura no stream para começar a consumir mensagens
  // automaticamente
  @Bean
  public Subscription subscription(StreamMessageListenerContainer<String, ObjectRecord<String, String>> container) {
    return container.receiveAutoAck(
        // Define o grupo de consumidores e o nome da instância
        Consumer.from("retry-group", "instance-1"),
        // Define de onde começar a ler (última mensagem consumida)
        StreamOffset.create("retry-stream", ReadOffset.lastConsumed()),
        // Listener responsável por processar as mensagens recebidas
        new StreamListener());
  }
}
