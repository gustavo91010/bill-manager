package com.ajudaqui.billmanager.service.sqs;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageListener {

  private final QueueService queueService;
  private final SqsService sqsService;

  public SqsMessageListener(SqsService sqsService, QueueService queueService) {
    this.sqsService = sqsService;
    this.queueService = queueService;
  }

  @Scheduled(fixedDelay = 60000) // 40 segundos
  public void receiveMessage() {
    for (String awsFila : queueService.queueNameList()) {
      CompletableFuture.runAsync(() -> sqsService.receiveMessage(awsFila));
    }
  }
}
