package com.ajudaqui.billmanager.service.sqs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SqsService.class.getSimpleName());
  @Value("${aws.id}")
  private String awsId;
  @Value("${aws.fila}")
  private String awsFila;
  @Value("${aws.region}")
  private String region;
  private final SqsClient sqsClient;

  public SqsService(SqsClient sqsClient) {
    this.sqsClient = sqsClient;
  }

  public void sendMessage(String messageBody) {
    String queueUrl = String.format("https://sqs.%s.amazonaws.com/%s/%s", region, awsId, awsFila);

    SendMessageRequest request = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        .messageBody(messageBody)
        .build();

    sqsClient.sendMessage(request);
  }

  public void receiveMessage() {
    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
        .queueUrl(awsFila)
        .waitTimeSeconds(20) // Tempo de espera para uma nova requisição a fila
        .maxNumberOfMessages(10) // Número máximo de mensagens a receber de uma vez
        .build();
    ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
    List<Message> messages = response.messages();
    if (messages.isEmpty()) {
      LOGGER.info("Não há mensagens para processar.");
    } else {
      for (Message message : messages) {

        LOGGER.info("Mensagem da fila {} recebida.", awsFila);
        deleteMessage(message);
      }
    }
  }

  private void deleteMessage(Message message) {
    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
        .queueUrl(awsFila)
        .receiptHandle(message.receiptHandle())
        .build();

    sqsClient.deleteMessage(deleteMessageRequest);
    LOGGER.info("Mensagem deletada: " + message.messageId());
  }

}
