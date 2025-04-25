package com.ajudaqui.billmanager.service.sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

@Service
public class QueueService {
  @Autowired
  private SqsClient sqsClient;

  public String createQueue(String queueName) {

    if (queueName == null || queueName.isEmpty()) {
      return "Erro: Nome da fila está vazio.";
    }
    if (queueName.length() > 80) {
      return "Erro: Nome da fila é muito longo.";
    }
    if (!queueName.matches("^[a-zA-Z0-9-_]+$")) {
      return "Erro: Nome da fila contém caracteres inválidos.";
    }
    CreateQueueRequest createStandardQueueRequest = CreateQueueRequest.builder()
        .queueName(queueName)
        .build();

    return sqsClient.createQueue(createStandardQueueRequest).queueUrl();
  }

  @SuppressWarnings("unchecked")
  public List<String> queueList() {
    List<String> response = new ArrayList<>();

    ListQueuesResponse listQueues = sqsClient.listQueues();
    Optional<?> queueUrls = listQueues.getValueForField("QueueUrls", List.class);

    if (queueUrls.isPresent() && !queueUrls.isEmpty()) {
      response = (List<String>) queueUrls.get();
    }
    return response;
  }

  public String deleteQueue(String queueName) {
    List<String> queueList = queueList();
    for (String urlSqs : queueList) {
      System.out.println(urlSqs);
      if (nameFile(urlSqs).equals(queueName)) {
        DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(urlSqs).build();
        sqsClient.deleteQueue(deleteQueueRequest);
        return String.format("Lista de nome: %s foi deletada com sucesso!", queueName);
      }

    }
    return String.format("Lista de nome: %s não localizada", queueName);
  }

  public Set<String> queueNameList() {
    return queueList().stream()
        .map(url -> nameFile(url))
        .collect(Collectors.toSet());
  }

  private String nameFile(String urlSqs) {
    String[] split = urlSqs.split("/");
    return split[4];
  }
}
