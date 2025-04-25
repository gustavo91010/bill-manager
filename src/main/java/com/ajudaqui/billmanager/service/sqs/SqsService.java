package com.ajudaqui.billmanager.service.sqs;

import java.util.List;

import com.ajudaqui.billmanager.exception.MsgException;

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

  @Value("${aws.region}")
  private String region;

  private final SqsClient sqsClient;
  private final QueueService queueService;

  public SqsService(SqsClient sqsClient, QueueService queueService) {
    this.sqsClient = sqsClient;
    this.queueService = queueService;
  }

  public void sendMessage(String awsFila, String messageBody) {
    checkingFila(awsFila);

    String queueUrl = String.format("https://sqs.%s.amazonaws.com/%s/%s", region, awsId, awsFila);
    SendMessageRequest request = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        .messageBody(messageBody)
        .build();

    sqsClient.sendMessage(request);
  }

  private void checkingFila(String awsFila) {
    if (awsFila.isEmpty()) {
      throw new MsgException("O campo fila é obrigatorio");
    }

    if (!queueService.queueNameList().contains(awsFila)) {
      throw new MsgException("Fila não registrada.");
    }
  }

  public void receiveMessage(String awsFila) {
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
        deleteMessage(awsFila, message);
      }
    }
  }

  private void deleteMessage(String awsFila, Message message) {
    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
        .queueUrl(awsFila)
        .receiptHandle(message.receiptHandle())
        .build();

    sqsClient.deleteMessage(deleteMessageRequest);
    LOGGER.info("Mensagem deletada: " + message.messageId());
  }

}
