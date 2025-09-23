package com.ajudaqui.billmanager.client.kafka.service;

import static java.util.Collections.singletonList;

import java.util.*;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.ajudaqui.billmanager.exception.MsgException;


import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaMonitorService {

  private final AdminClient admin;
  private final String kafkaServer;

  public KafkaMonitorService(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
    this.kafkaServer = kafkaServer;
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
    this.admin = AdminClient.create(props);
  }

  public Map<String, Object> getClusterInfo() {
    DescribeClusterResult result = admin.describeCluster();
    Map<String, Object> data = new HashMap<>();
    try {
      data.put("clusterId", result.clusterId().get());
      data.put("controller", result.controller().get().idString());
      data.put("nodes", result.nodes().get().toString());

    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na consulta dos clusters: " + e.getMessage());
    }
    return data;
  }

  private DescribeTopicsResult activesTopics() {
    Map<String, Object> response = new HashMap<>();
    Set<String> topics = new HashSet<>();
    try {
      topics = admin.listTopics().names().get();
      response.put("topics", topics);

    } catch (InterruptedException | ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return admin.describeTopics(topics);
  }

  public Map<String, Object> getTopicsInfo() {
    Map<String, Object> response = new HashMap<>();

    try {
      Map<String, Object> details = new HashMap<>();
      DescribeTopicsResult desc = activesTopics();
      desc.all().get().forEach((name, t) -> {
        Map<String, Object> info = new HashMap<>();

        info.put("partitions", t.partitions().size());
        info.put("replicationFactor", t.partitions().get(0).replicas().size());

        details.put(name, info);
      });

      response.put("details", details);
    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na busca dos topicos: " + e.getMessage());
    }

    return response;
  }

  public Map<String, Object> prooffset(String topicName) {
    Map<String, Object> response = new HashMap<>();

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps())) {
      List<TopicPartition> partitions = new ArrayList<>();
      consumer.partitionsFor(topicName)
          .forEach(info -> partitions.add(new TopicPartition(info.topic(), info.partition())));

      Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
      //Pega a a lista dos ofsets com o numero tal de mensagens e soma
      long totalMensagens = endOffsets.values().stream().mapToLong(Long::longValue).sum();

      response.put("Nome", topicName);
      response.put("totalMensagens", totalMensagens);
    }

    return response;
  }

  public Map<String, String> criarTopico(String name, int numPartitions, short replicationFactor) {
    try {
      NewTopic newTopic = new NewTopic(name, numPartitions, replicationFactor);
      admin.createTopics(singletonList(newTopic)).all().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na criação do topico: " + name + " Motivo: " + e.getMessage());
    }
    return Map.of("message", "Topico name: " + name + " criado com sucesso.");
  }

  public Map<String, String> deleteTopico(String name) {
    try {
      admin.deleteTopics(singletonList(name)).all().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na exclusão  do topico: " + name + " Motivo: " + e.getMessage());
    }
    return Map.of("message", "Topico name: " + name + " excluido com sucesso.");
  }

  public Map<String, Object> allConsumers() {
    Map<String, Object> response = new HashMap<>();
    try {
      Collection<ConsumerGroupListing> collection = admin.listConsumerGroups().all().get();
      for (ConsumerGroupListing consumer : collection) {
        String groupId = consumer.groupId();
        response.put("groupId", groupId);
        // Pega a descrição do grupo
        DescribeConsumerGroupsResult result = admin.describeConsumerGroups(singletonList(groupId));

        Map<String, ConsumerGroupDescription> map = result.all().get();
        ConsumerGroupDescription consumerDescription = map.get(groupId);
        response.put("Estado", consumerDescription.state());
        consumerDescription.members().forEach(member -> {
          member.assignment().topicPartitions().forEach(topicoPartitions -> {
            response.put("Topico", topicoPartitions.topic());
            response.put("partitions", topicoPartitions.partition());

          });
        });
      }

    } catch (Exception e) {
      throw new MsgException("Erro na exclusão  do topico sono! Motivo: " + e.getMessage());
    }
    return response;
  }

  private Properties consumerProps() {
    Properties consumerProps = new Properties();
    // consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
    // "44.219.162.47:9092"); // ou use @Value
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer); // ou use @Value
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "offset-monitor");
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // garante pegar offset inicial se nunca
                                                                            // consumiu
    return consumerProps;
  }
}
