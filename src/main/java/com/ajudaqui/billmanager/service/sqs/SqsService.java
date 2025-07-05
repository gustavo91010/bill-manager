package com.ajudaqui.billmanager.service.sqs;

import java.util.List;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.service.UsersService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private UsersService usersService;

  private final SqsClient sqsClient;

  public SqsService(SqsClient sqsClient) {
    this.sqsClient = sqsClient;
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
  }

  public void receiveMessage(String awsFila) {
    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
        .queueUrl(awsFila)
        .waitTimeSeconds(20) // Tempo de espera para uma nova requisição a fila
        .maxNumberOfMessages(10) // Número máximo de mensagens a receber de uma vez
        .build();
    ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
    List<Message> messages = response.messages();

    if (!messages.isEmpty()) {
      for (Message message : messages) {
        if (isBillMessage(message.body())) {

          LOGGER.info("Mensagem da fila {} recebida.", awsFila);
          usersService.registerUserBySqs(message.body());

          deleteMessage(awsFila, message);
          LOGGER.info("Mensagem id {} processada.", message.messageId());
        }
      }
    }
  }

  private boolean isBillMessage(String message) {

    JsonElement messageJson = JsonParser.parseString(message);
    String application = messageJson.getAsJsonObject().get("application").getAsString();
    return application.equals("bill-manager");
  }

  private void deleteMessage(String awsFila, Message message) {
    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
        .queueUrl(awsFila)
        .receiptHandle(message.receiptHandle())
        .build();

    sqsClient.deleteMessage(deleteMessageRequest);
  }

  public Users create(String authorization) {
    if (!"lalala-tocomsono-oiuygoysai754rfio8y9p8".equals(authorization))
      throw new MsgException("Solicitação não autorizada");

    return usersService.create();
  }

}
