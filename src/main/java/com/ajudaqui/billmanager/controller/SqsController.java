package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.service.sqs.QueueService;
import com.ajudaqui.billmanager.service.sqs.SqsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.sqs.endpoints.internal.Value.Str;

@RestController
@RequestMapping("/sqs")
public class SqsController {

  @Autowired
  private SqsService sqsProducerService;

  @Autowired
  private QueueService queueService;

  @PostMapping("/send-message")
  public String senMessage(@RequestBody String message) {
    sqsProducerService.sendMessage(message);
    return message;
  }

  @PostMapping("/queue")
  public String createQueue(@RequestParam String queueName) {
  
    return queueService.createQueue(queueName);
  }

}
