package com.ajudaqui.billmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsProducerService {

  @Value("${aws.id}")
  private String awsId;
  @Value("${aws.fila}")
  private String awsFila;
  @Value("${aws.region}")
  private String region;
  private final SqsClient sqsClient;

  public SqsProducerService(SqsClient sqsClient) {
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
}
