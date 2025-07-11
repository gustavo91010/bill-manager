package com.ajudaqui.billmanager.service.sqs;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageListener {

  private final SqsService sqsService;

  @Value("${aws.fila}")
  private String awsFila;

  public SqsMessageListener(SqsService sqsService) {
    this.sqsService = sqsService;
  }

  @Scheduled(fixedDelay = 60000)
  public void receiveMessage() {
    CompletableFuture.runAsync(() -> sqsService.receiveMessage(awsFila));
  }
}
