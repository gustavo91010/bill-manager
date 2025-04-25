package com.ajudaqui.billmanager.service.sqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.SqsException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

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
}
