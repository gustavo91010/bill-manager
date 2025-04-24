package com.ajudaqui.billmanager.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageListener {

  private final SqsService sqsService;

  public SqsMessageListener(SqsService sqsService) {
    this.sqsService = sqsService;
  }

  @Scheduled(fixedDelay = 40000) // 40 segundos
  public void receiveMessage() {
    sqsService.receiveMessage();
  }
}
