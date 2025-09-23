package com.ajudaqui.billmanager.service;

import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.ajudaqui.billmanager.exception.MsgException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaMonitorService {

  private final AdminClient admin;

  public KafkaMonitorService(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
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

  public Map<String, Object> prooffset() throws InterruptedException, ExecutionException {
    Map<String, Object> response = new HashMap<>();
    DescribeTopicsResult topicos = activesTopics();

    topicos.all().get().forEach((name, t) -> {
      response.put("Nome: ", name);
      response.put("total de mensagegs ", t.partitions().size());
    });
    return response;
  }

  public void criarTopico(String name, int numPartitions, short replicationFactor) {
    try {
      NewTopic newTopic = new NewTopic(name, numPartitions, replicationFactor);
      admin.createTopics(singletonList(newTopic)).all().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na criação do topico: " + name + " Motivo: " + e.getMessage());
    }
  }

  public void deleteTopico(String name) {
    try {
      admin.deleteTopics(singletonList(name)).all().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new MsgException("Erro na exclusão  do topico: " + name + " Motivo: " + e.getMessage());
    }
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
}
